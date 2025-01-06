package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.repository.*;
import com.bancos.cuentasbancarias.service.DebtCheckService;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {
    @Mock
    private ClientDAO clientDAO;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private ClientTypeDAO clientTypeDAO;

    @Mock
    private AccountTypeDAO accountTypeDAO;

    @Mock
    private CreditCardDAO creditCardDAO;

    @Mock
    private CreditDAO creditDAO;

    @Mock
    private DebtCheckService debtCheckService;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientType clientType;
    private Account account;
    private AccountType accountType;

    @BeforeEach
    void setUp() {
        clientType = new ClientType();
        clientType.setId(new ObjectId());
        clientType.setNombre("PERSONA");

        client = new Client();
        client.setId(new ObjectId("677bfea0585e235b1c201682"));
        client.setNombre("Cesar Huanes");
        client.setClientTypeId(clientType.getId());
        client.setClientType(clientType);

        accountType = new AccountType();
        accountType.setId(new ObjectId());
        accountType.setNombre("CUENTA_AHORRO");

        account = new Account();
        account.setId(new ObjectId());
        account.setClienteId(client.getId());
        account.setAccountTypeId(accountType.getId());
        account.setAccountType(accountType);
    }

    @Test
    void testFindAll() {
        when(clientDAO.findAll()).thenReturn(Flux.just(client));
        when(clientTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(clientType));

        Flux<Client> result = clientService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(c -> c.getNombre().equals("Cesar Huanes") && c.getClientType().getNombre().equals("PERSONA"))
                .verifyComplete();
    }

    @Test
    void testFindById() {
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(clientTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(clientType));

        Mono<Client> result = clientService.findById(client.getId().toString());

        StepVerifier.create(result)
                .expectNextMatches(c -> c.getNombre().equals("Cesar Huanes") && c.getClientType().getNombre().equals("PERSONA"))
                .verifyComplete();
    }

    @Test
    void testSave() {
        when(clientTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(clientType));
        when(clientDAO.save(any(Client.class))).thenReturn(Mono.just(client));

        Mono<Client> result = clientService.save(client);

        StepVerifier.create(result)
                .expectNext(client)
                .verifyComplete();
    }

    @Test
    void testDeleteById() {
        when(clientDAO.deleteById(any(ObjectId.class))).thenReturn(Mono.empty());

        Mono<Void> result = clientService.deleteById(client.getId().toString());

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void testSaveCuentaByCliente_NoOverdueDebt() {
        when(debtCheckService.hasOverdueDebt(any(ObjectId.class))).thenReturn(Mono.just(false));
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(accountTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(accountType));
        when(accountDAO.saveAll(any(List.class))).thenReturn(Flux.just(account));
        when(clientDAO.save(any(Client.class))).thenReturn(Mono.just(client));

        Mono<List<Account>> result = clientService.saveCuentaByCliente(client.getId().toString(), Collections.singletonList(account));

        StepVerifier.create(result)
                .expectNextMatches(accounts -> accounts.size() == 1 && accounts.get(0).getAccountType().getNombre().equals("CUENTA_AHORRO"))
                .verifyComplete();
    }

    @Test
    void testSaveCuentaByCliente_WithOverdueDebt() {
        when(debtCheckService.hasOverdueDebt(any(ObjectId.class))).thenReturn(Mono.just(true));

        Mono<List<Account>> result = clientService.saveCuentaByCliente(client.getId().toString(), Collections.singletonList(account));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ValidationException && throwable.getMessage().equals("El cliente tiene deudas vencidas y no puede crear nuevas cuentas."))
                .verify();
    }
}
