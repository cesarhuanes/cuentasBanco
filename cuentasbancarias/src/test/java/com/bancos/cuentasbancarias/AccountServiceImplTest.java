package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.service.DebtCheckService;
import com.bancos.cuentasbancarias.service.impl.AccountServiceImpl;
import jakarta.validation.ValidationException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
    @Mock
    private AccountDAO accountDAO;

    @Mock
    private AccountTypeDAO accountTypeDAO;

    @Mock
    private ClientDAO clientDAO;

    @Mock
    private DebtCheckService debtCheckService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private Client client;
    private AccountType accountType;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(new ObjectId("677bfea0585e235b1c201682"));
        client.setNombre("Cesar Huanes");

        accountType = new AccountType();
        accountType.setId(new ObjectId());
        accountType.setNombre("CUENTA_AHORRO");

        account = new Account();
        account.setId(new ObjectId());
        account.setClienteId(client.getId());
        account.setAccountTypeId(accountType.getId());
        account.setAccountType(accountType);
        account.setSaldo(1000.0);
    }

    @Test
    void testCreateCuenta_NoOverdueDebt() {
        when(debtCheckService.hasOverdueDebt(any(ObjectId.class))).thenReturn(Mono.just(false));
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(accountTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(accountType));
        when(accountDAO.save(any(Account.class))).thenReturn(Mono.just(account));

        Mono<Account> result = accountService.createCuenta(account);

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void testCreateCuenta_WithOverdueDebt() {
        when(debtCheckService.hasOverdueDebt(any(ObjectId.class))).thenReturn(Mono.just(true));

        Mono<Account> result = accountService.createCuenta(account);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ValidationException && throwable.getMessage().equals("El cliente tiene deudas vencidas y no puede crear nuevas cuentas."))
                .verify();
    }

    @Test
    void testGetCuentaById() {
        when(accountDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(account));

        Mono<Account> result = accountService.getCuentaById(account.getId().toString());

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void testGetAllCuentas() {
        when(accountDAO.findAll()).thenReturn(Flux.just(account));

        Flux<Account> result = accountService.getAllCuentas();

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void testUpdateCuenta() {
        when(accountDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(account));
        when(accountDAO.save(any(Account.class))).thenReturn(Mono.just(account));
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(accountTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(accountType));
        when(debtCheckService.hasOverdueDebt(any(ObjectId.class))).thenReturn(Mono.just(false));

        Mono<Account> result = accountService.updateCuenta(account.getId().toString(), account);

        StepVerifier.create(result)
                .expectNext(account)
                .verifyComplete();
    }

    @Test
    void testDeleteCuenta() {
        when(accountDAO.delete(any(Account.class))).thenReturn(Mono.empty());

        Mono<Void> result = accountService.deleteCuenta(account);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testGetAccountBalance() {
        when(accountDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(account));

        Mono<Double> result = accountService.getAccountBalance(account.getId().toString());

        StepVerifier.create(result)
                .expectNext(1000.0)
                .verifyComplete();
    }

    @Test
    void testGetAccountBalance_NotFound() {
        when(accountDAO.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        Mono<Double> result = accountService.getAccountBalance(account.getId().toString());

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Account not found"))
                .verify();
    }
}
