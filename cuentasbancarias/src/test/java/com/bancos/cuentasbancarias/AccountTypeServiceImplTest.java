package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.service.impl.AccountTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(MockitoExtension.class)
public class AccountTypeServiceImplTest {
    @Mock
    private AccountTypeDAO accountTypeDAO;

    @InjectMocks
    private AccountTypeServiceImpl accountTypeService;

    private AccountType accountType;

    @BeforeEach
    void setUp() {
        accountType = new AccountType();
        accountType.setNombre("CUENTA_AHORRO");
    }

    @Test
    void testSave_Success() {
        when(accountTypeDAO.save(any(AccountType.class))).thenReturn(Mono.just(accountType));

        Mono<AccountType> result = accountTypeService.save(accountType);

        StepVerifier.create(result)
                .expectNext(accountType)
                .verifyComplete();
    }

    @Test
    void testSave_Error() {
        when(accountTypeDAO.save(any(AccountType.class))).thenReturn(Mono.error(new RuntimeException("Error saving AccountType")));

        Mono<AccountType> result = accountTypeService.save(accountType);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Error saving AccountType"))
                .verify();
    }
}
