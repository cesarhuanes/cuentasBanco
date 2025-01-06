package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.CreditCardService;
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

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CreditServiceImplTest {
    @Mock
    private CreditDAO creditDAO;

    @Mock
    private CreditTypeDAO creditTypeDAO;

    @Mock
    private ClientDAO clientDAO;

    @Mock
    private CreditCardDAO creditCardDAO;

    @Mock
    private CreditCardService creditCardService;

    @InjectMocks
    private CreditServiceImpl creditService;

    private Credit credit;
    private Client client;
    private CreditType creditType;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(new ObjectId("677bfea0585e235b1c201682"));
        client.setNombre("Cesar Huanes");

        creditType = new CreditType();
        creditType.setId(new ObjectId());
        creditType.setNombreCredito("TARJETA_CREDITO");

        credit = new Credit();
        credit.setId(new ObjectId("677bfea0585e235b1c201683"));
        credit.setClientId(client.getId());
        credit.setCreditTypeId(creditType.getId());
        credit.setAmount(500.0);
        credit.setAmountAvailable(200.0);
        credit.setDueDate( new Date());

        creditCard = new CreditCard();
        creditCard.setClientId(client.getId());
        creditCard.setLimitCredit(500.0);
        creditCard.setAmountAviable(200.0);
    }

    @Test
    void testSaveCredit() {
        when(clientDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(client));
        when(creditTypeDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(creditType));
        when(creditCardService.saveCreditCard(any(CreditCard.class))).thenReturn(Mono.just(creditCard));
        when(creditDAO.save(any(Credit.class))).thenReturn(Mono.just(credit));

        Mono<Credit> result = creditService.saveCredit(credit);

        StepVerifier.create(result)
                .expectNext(credit)
                .verifyComplete();
    }

    @Test
    void testUpdateCredit() {
        ObjectId creditId = new ObjectId("677bfea0585e235b1c201683");
        Credit updatedCredit = new Credit();
        updatedCredit.setAmount(600.0);
        updatedCredit.setAmountAvailable(300.0);
        updatedCredit.setCreditType(creditType);
        updatedCredit.setClientId(client.getId());

        when(creditDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(credit));
        when(creditDAO.save(any(Credit.class))).thenReturn(Mono.just(updatedCredit));
        when(creditCardDAO.findByClientId(any(ObjectId.class))).thenReturn(Flux.just(creditCard));
        when(creditCardDAO.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        Mono<Credit> result = creditService.updateCredit(creditId.toString(), updatedCredit);

        StepVerifier.create(result)
                .expectNextMatches(savedCredit -> savedCredit.getAmount() == 600.0 && savedCredit.getAmountAvailable() == 300.0)
                .verifyComplete();
    }
}
