package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.service.ClientService;
import com.bancos.cuentasbancarias.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final ClientService clientService;
    private final CreditCardDAO creditCardDAO;

    @Override
    public Mono<CreditCard> saveCreditCard(CreditCard creditCard) {
         return  clientService.findById(creditCard.getClientId().toString())
                 .flatMap(client -> {
                     creditCard.setClient(client);
                     return creditCardDAO.save(creditCard);
                 });

    }
}
