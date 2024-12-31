package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.CreditService;
import lombok.AllArgsConstructor;
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
    @Override
    public Mono<Credit> saveCredit(Credit credit) {
        return clientDAO.findById(credit.getClientId())
                .flatMap(client -> {
                   credit.setClient(client);
                   return creditTypeDAO.findById(credit.getCreditTypeId())
                           .map(creditType -> {
                               credit.setCreditType(creditType);
                              return credit;
                           });
                })
                .flatMap(creditDAO::save);
    }
}
