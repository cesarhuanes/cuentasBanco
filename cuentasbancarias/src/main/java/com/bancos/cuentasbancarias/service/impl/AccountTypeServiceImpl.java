package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.service.AccountTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Slf4j
@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    @Autowired
    private AccountTypeDAO accountTypeDAO;
    @Override
    public Mono<AccountType> save(AccountType accountType) {
        return accountTypeDAO.save(accountType)
                .doOnSuccess(saveAccounType->{
                    log.info("AccountType saved successfully:",saveAccounType);
                })
                .doOnError(error->{
                    log.info("Error saving AccountType:",error.getMessage());

                });

    }
}
