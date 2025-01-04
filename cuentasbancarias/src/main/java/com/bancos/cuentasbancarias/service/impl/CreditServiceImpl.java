package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.CreditCardService;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.util.Constants;
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
                               if(creditType.getNombreCredito().equals(Constants.TARJETA_CREDITO)){
                                   CreditCard creditCard = new CreditCard();
                                   creditCard.setClientId(credit.getClientId());
                                   creditCard.setLimitCredit(credit.getAmount());
                                   creditCard.setAmountAviable(credit.getAmountAvailable());
                                   return creditCardService.saveCreditCard(creditCard)
                                           .thenReturn(credit);
                               }
                               return Mono.just(credit);
                           });
                })
                .flatMap(creditDAO::save);
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


}
