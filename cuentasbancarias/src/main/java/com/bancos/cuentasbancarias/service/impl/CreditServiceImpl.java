package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ClientDAO;
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
                                   creditCard.setAmountAviable(credit.getAmountAviable());
                                   return creditCardService.saveCreditCard(creditCard)
                                           .thenReturn(credit);
                               }
                               return Mono.just(credit);
                           });
                })
                .flatMap(creditDAO::save);
    }


}
