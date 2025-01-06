package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.Signature;
import com.bancos.cuentasbancarias.repository.PersonDAO;
import com.bancos.cuentasbancarias.repository.SignatureDAO;
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
public class SignatureServiceImplTest {
    @Mock
    private SignatureDAO signatureDAO;

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private SignatureServiceImpl signatureService;

    private Signature signature;

    @BeforeEach
    void setUp() {
        signature = new Signature();
        // Configura los atributos necesarios de la firma
    }

    @Test
    void testSaveSignature() {
        when(personDAO.save(any(Signature.class))).thenReturn(Mono.just(signature));
        when(signatureDAO.save(any(Signature.class))).thenReturn(Mono.just(signature));

        Mono<Signature> result = signatureService.saveSignature(signature);

        StepVerifier.create(result)
                .expectNext(signature)
                .verifyComplete();
    }
}
