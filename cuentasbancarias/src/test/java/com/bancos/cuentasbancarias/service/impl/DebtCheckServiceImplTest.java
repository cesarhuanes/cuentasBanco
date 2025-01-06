package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.repository.CreditDAO;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DebtCheckServiceImplTest {
    @Mock
    private CreditDAO creditDAO;

    @InjectMocks
    private DebtCheckServiceImpl debtCheckService;

    private ObjectId clientId;

    @BeforeEach
    void setUp() {
        clientId = new ObjectId();
    }

    @Test
    void testHasOverdueDebt_NoOverdueDebt() {
        when(creditDAO.findByClientId(clientId)).thenReturn(Flux.empty());

        Mono<Boolean> result = debtCheckService.hasOverdueDebt(clientId);

        StepVerifier.create(result)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void testHasOverdueDebt_WithOverdueDebt() {
        Credit overdueCredit = new Credit();
        overdueCredit.setOverdue(true);

        when(creditDAO.findByClientId(clientId)).thenReturn(Flux.just(overdueCredit));

        Mono<Boolean> result = debtCheckService.hasOverdueDebt(clientId);

        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();
    }
}
