package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.dto.AccountResponse;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.service.AccountService;
import com.bancos.cuentasbancarias.service.DebtCheckService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountDAO accountDAO;
    private final AccountTypeDAO accountTypeDAO;
    private final ClientDAO clientDAO;
    private final DebtCheckService debtCheckService;


    @Override
    public Mono<Account> createCuenta(Account account) {
        return debtCheckService.hasOverdueDebt(account.getClienteId())
                .flatMap(hasOverdueDebt -> {
                    if (hasOverdueDebt) {
                        return Mono.error(new ValidationException("El cliente tiene deudas vencidas y no puede crear nuevas cuentas."));
                    } else {
                        return clientDAO.findById(account.getClienteId())
                                .flatMap(client -> {
                                    account.setClient(client);
                                    return accountTypeDAO.findById(account.getAccountTypeId())
                                            .map(accountType -> {
                                                account.setAccountType(accountType);
                                                return account;
                                            })
                                            .flatMap(accountDAO::save);
                                });
                    }
                });
    }

    @Override
    public Mono<Account> getCuentaById(String id) {
        ObjectId objectId = new ObjectId(id);
        return accountDAO.findById(objectId);

    }

    @Override
    public Mono<AccountResponse> getAccountById(String id) {
        return this.getCuentaById(id).map(account -> {
            AccountResponse response=new AccountResponse();
            response.setId(account.getId().toString());
            response.setClient(account.getClient().getNombre());
            response.setTypeAccount(account.getAccountType().getNombre());
            response.setSaldo(account.getSaldo());
            response.setTypeClient(account.getClient().getClientType().getNombre());
            return response;
        });
    }

    @Override
    public Flux<Account> getAllCuentas() {

        return accountDAO.findAll();
    }

    @Override
    public Mono<Account> updateCuenta(String id, Account account) {
        ObjectId objectId=new ObjectId(id);
        return accountDAO.findById(objectId)
                .flatMap(existeCuenta->{
                    existeCuenta.setSaldo(account.getSaldo());
                    existeCuenta.setAccountType(account.getAccountType());
                    existeCuenta.setClienteId(account.getClienteId());
                    return createCuenta(existeCuenta);
                });
    }

    @Override
    public Mono<Void> deleteCuenta(Account account) {
        return accountDAO.delete(account);
    }

    @Override
    public Mono<Double> getAccountBalance(String accountId) {
        var  accountID=new ObjectId(accountId);
        return accountDAO.findById(accountID)
                .map(Account::getSaldo)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }
}
