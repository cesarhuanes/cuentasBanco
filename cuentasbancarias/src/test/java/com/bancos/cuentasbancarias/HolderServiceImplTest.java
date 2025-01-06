package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.Holder;
import com.bancos.cuentasbancarias.repository.HolderDAO;
import com.bancos.cuentasbancarias.repository.PersonDAO;
import com.bancos.cuentasbancarias.service.impl.HolderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class HolderServiceImplTest {
    @Mock
    private HolderDAO holderDAO;

    @Mock
    private PersonDAO personDAO;

    @InjectMocks
    private HolderServiceImpl holderService;

    private Holder holder;

    @BeforeEach
    void setUp() {
        holder = new Holder();
        // Configura los atributos necesarios del holder
    }

    @Test
    void testSaveHolder() {
        when(personDAO.save(any(Holder.class))).thenReturn(Mono.just(holder));
        when(holderDAO.save(any(Holder.class))).thenReturn(Mono.just(holder));

        Mono<Holder> result = holderService.saveHolder(holder);

        StepVerifier.create(result)
                .expectNext(holder)
                .verifyComplete();
    }
}
