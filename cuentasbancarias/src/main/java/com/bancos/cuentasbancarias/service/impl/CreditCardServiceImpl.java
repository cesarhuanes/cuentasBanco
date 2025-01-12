package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.service.ClientService;
import com.bancos.cuentasbancarias.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class CreditCardServiceImpl implements CreditCardService {
    private static final Logger logger = LoggerFactory.getLogger(CreditCardServiceImpl.class);

    private final ClientDAO clientDAO;
    private final CreditCardDAO creditCardDAO;

    @Override
    public Mono<CreditCard> saveCreditCard(CreditCard creditCard) {
        logger.info("CreditCardServiceImpl.creditCard={}",creditCard.getClientId().toString());
         return  clientDAO.findById(creditCard.getClientId())
                 .flatMap(client -> {
                     creditCard.setClient(client);
                     logger.info("CreditCardServiceImpl.saveCreditCard");
                     return creditCardDAO.save(creditCard);
                 });

    }
    @Override
    public Mono<Double> getCreditCardBalance(String creditCardId) {
        return creditCardDAO.findById(creditCardId)
                .map(CreditCard::getAmountAviable)
                .switchIfEmpty(Mono.error(new RuntimeException("Credit card not found")));
    }
}
