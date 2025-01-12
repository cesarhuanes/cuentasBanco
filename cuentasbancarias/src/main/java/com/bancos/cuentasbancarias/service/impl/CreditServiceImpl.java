package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.CreditCardService;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.util.Constants;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class CreditServiceImpl implements CreditService {
    private static final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);

    private final CreditDAO creditDAO;
    private final CreditTypeDAO creditTypeDAO;
    private final ClientDAO clientDAO;
    private final CreditCardDAO creditCardDAO;
    private final CreditCardService creditCardService;
    @Override
    public Mono<Credit> saveCredit(Credit credit) {
        return clientDAO.findById(credit.getClientId())
                .flatMap(client -> {
                    credit.setClient(client);
                    return creditTypeDAO.findById(credit.getCreditTypeId())
                            .flatMap(creditType -> {
                                credit.setCreditType(creditType);
                                credit.updateOverdueStatus();
                                logger.info("CreditServiceImpl.saveCredit.creditType={}",creditType.getNombreCredito());
                                if (creditType.getNombreCredito().equals(Constants.TARJETA_CREDITO)) {
                                    return handleCreditCard(credit);
                                }
                                return Mono.just(credit);
                            });
                })
                .flatMap(this::validateAndSaveCredit);
    }

    @Override
    public Mono<Credit> updateCredit(String id, Credit credit) {
        ObjectId objectId = new ObjectId(id);
        return creditDAO.findById(objectId)
                .flatMap(existingCredit -> {
                    existingCredit.setAmount(credit.getAmount());
                    existingCredit.setAmountAvailable(credit.getAmountAvailable());
                    existingCredit.setCreditType(credit.getCreditType());
                    existingCredit.setClientId(credit.getClientId());

                    return creditDAO.save(existingCredit)
                            .flatMap(savedCredit -> {
                                // Verificar si el tipo de crédito es "TARJETA_CREDITO"
                                if (Constants.TARJETA_CREDITO.equals(savedCredit.getCreditType().getNombreCredito())) {
                                    return creditCardDAO.findByClientId(savedCredit.getClientId())
                                            .flatMap(creditCard -> {
                                                creditCard.setLimitCredit(savedCredit.getAmount());
                                                creditCard.setAmountAviable(savedCredit.getAmountAvailable());
                                                return creditCardDAO.save(creditCard);
                                            })
                                            .then(Mono.just(savedCredit));
                                } else {
                                    return Mono.just(savedCredit);
                                }
                            });
                });
    }
    private Mono<Credit> handleCreditCard(Credit credit) {
        CreditCard creditCard = new CreditCard();
        creditCard.setClientId(credit.getClientId());
        creditCard.setLimitCredit(credit.getAmount());
        creditCard.setAmountAviable(credit.getAmountAvailable());
        logger.info("CreditServiceImpl.handleCreditCard ={}",credit.getClientId());
        return creditCardService.saveCreditCard(creditCard)
                .thenReturn(credit);
    }
    private Mono<Credit> validateAndSaveCredit(Credit credit) {
        logger.info("validateAndSaveCredit.tipoCliente={}",credit.getClient().getClientType().getNombre());
        if (credit.getClient().getClientType().getNombre().equals(Constants.TIPO_CLIENTE_PERSONA)) {
            return validatePersonalClientCredit(credit);
        } else if (credit.getClient().getClientType().getNombre().equals(Constants.TIPO_CLIENTE_EMPRESA)) {
            return validateBusinessClientCredit(credit);
        } else {
            return Mono.error(new ValidationException("Tipo de cliente no reconocido."));
        }
    }

    private Mono<Credit> validatePersonalClientCredit(Credit credit) {
        ObjectId clientId = credit.getClientId();
        return creditDAO.findByClientId(clientId)
                .filter(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL) ||
                        existingCredit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO) ||
                        existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_EMPRESARIAL))
                .collectList()
                .flatMap(existingCredits -> {
                    boolean hasPersonalCredit = existingCredits.stream()
                            .anyMatch(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL));
                    boolean hasCreditCard = existingCredits.stream()
                            .anyMatch(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO));
                    boolean hasBusinessCredit = existingCredits.stream()
                            .anyMatch(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_EMPRESARIAL));

                    if (credit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL) && hasPersonalCredit) {
                        return Mono.error(new ValidationException("Solo se permite un crédito personal por persona."));
                    } else if (credit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO) && hasCreditCard) {
                        return Mono.error(new ValidationException("Solo se permite una tarjeta de crédito por persona."));
                    } else if (credit.getCreditType().getNombreCredito().equals(Constants.CREDIT_EMPRESARIAL)) {
                        return Mono.error(new ValidationException("Un cliente de tipo persona no puede tener un crédito empresarial."));
                    } else {
                        return creditDAO.save(credit);
                    }
                });
    }

    private Mono<Credit> validateBusinessClientCredit(Credit credit) {
        ObjectId clientId = credit.getClientId();
        return creditDAO.findByClientId(clientId)
                .filter(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_EMPRESARIAL) ||
                        existingCredit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO) ||
                        existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL))
                .collectList()
                .flatMap(existingCredits -> {
                    boolean hasCreditCard = existingCredits.stream()
                            .anyMatch(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO));
                    boolean hasPersonalCredit = existingCredits.stream()
                            .anyMatch(existingCredit -> existingCredit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL));

                    if (credit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO) && hasCreditCard) {
                        return Mono.error(new ValidationException("Solo se permite una tarjeta de crédito por empresa."));
                    } else if (credit.getCreditType().getNombreCredito().equals(Constants.CREDIT_PERSONAL)) {
                        return Mono.error(new ValidationException("Un cliente de tipo empresa no puede tener un crédito personal."));
                    } else {
                        return creditDAO.save(credit);
                    }
                });
    }
}
