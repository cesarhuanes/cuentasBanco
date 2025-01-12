package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.service.ClientService;
import org.bson.types.ObjectId;
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
public class CreditCardServiceImplTest {
    @Mock
    private ClientDAO clientDAO;

    @Mock
    private CreditCardDAO creditCardDAO;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    private CreditCard creditCard;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(new ObjectId("677bfea0585e235b1c201682"));
        client.setNombre("Cesar Huanes");

        creditCard = new CreditCard();
        creditCard.setClientId(client.getId());
        creditCard.setLimitCredit(500.0);
        creditCard.setAmountAviable(200.0);
    }

    @Test
    void testSaveCreditCard() {
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(creditCardDAO.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        Mono<CreditCard> result = creditCardService.saveCreditCard(creditCard);

        StepVerifier.create(result)
                .expectNext(creditCard)
                .verifyComplete();
    }

    @Test
    void testGetCreditCardBalance() {
        String creditCardId = "677bfea0585e235b1c201683";

        when(creditCardDAO.findById(any(String.class))).thenReturn(Mono.just(creditCard));

        Mono<Double> result = creditCardService.getCreditCardBalance(creditCardId);

        StepVerifier.create(result)
                .expectNext(200.0)
                .verifyComplete();
    }

    @Test
    void testGetCreditCardBalance_NotFound() {
        String creditCardId = "677bfea0585e235b1c201683";

        when(creditCardDAO.findById(any(String.class))).thenReturn(Mono.empty());

        Mono<Double> result = creditCardService.getCreditCardBalance(creditCardId);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Credit card not found"))
                .verify();
    }
}
