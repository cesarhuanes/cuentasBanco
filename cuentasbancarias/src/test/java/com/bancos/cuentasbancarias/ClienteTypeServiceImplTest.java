package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.repository.ClientTypeDAO;
import com.bancos.cuentasbancarias.service.impl.ClienteTypeServiceImpl;
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
public class ClienteTypeServiceImplTest {
    @Mock
    private ClientTypeDAO clientTypeDAO;

    @InjectMocks
    private ClienteTypeServiceImpl clienteTypeService;

    private ClientType clientType;

    @BeforeEach
    void setUp() {
        clientType = new ClientType();
        clientType.setNombre("PERSONA");
    }

    @Test
    void testSave() {
        when(clientTypeDAO.save(any(ClientType.class))).thenReturn(Mono.just(clientType));

        Mono<ClientType> result = clienteTypeService.save(clientType);

        StepVerifier.create(result)
                .expectNext(clientType)
                .verifyComplete();
    }
}
