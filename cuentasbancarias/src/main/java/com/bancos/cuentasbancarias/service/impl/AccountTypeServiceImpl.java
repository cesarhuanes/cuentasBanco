package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.service.AccountTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class AccountTypeServiceImpl implements AccountTypeService {
    private static final Logger logger = LoggerFactory.getLogger(AccountTypeServiceImpl.class);
    private final AccountTypeDAO accountTypeDAO;

    @Override
    public Mono<AccountType> save(AccountType accountType) {
        return accountTypeDAO.save(accountType)
                .doOnSuccess(saveAccounType->{
                    logger.info("AccountType saved successfully:",saveAccounType);
                })
                .doOnError(error->{
                    logger.info("Error saving AccountType:",error.getMessage());

                });

    }
}
