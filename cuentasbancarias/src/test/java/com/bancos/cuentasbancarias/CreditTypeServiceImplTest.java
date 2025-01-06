package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.impl.CreditTypeServiceImpl;
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

@ExtendWith(MockitoExtension.class)
public class CreditTypeServiceImplTest {
    @Mock
    private CreditTypeDAO creditTypeDAO;

    @InjectMocks
    private CreditTypeServiceImpl creditTypeService;

    private CreditType creditType;

    @BeforeEach
    void setUp() {
        creditType = new CreditType();
        creditType.setNombreCredito("Personal Loan");
    }

    @Test
    void testSaveCreditType() {
        when(creditTypeDAO.save(any(CreditType.class))).thenReturn(Mono.just(creditType));

        Mono<CreditType> result = creditTypeService.saveCreditType(creditType);

        StepVerifier.create(result)
                .expectNext(creditType)
                .verifyComplete();
    }
}
