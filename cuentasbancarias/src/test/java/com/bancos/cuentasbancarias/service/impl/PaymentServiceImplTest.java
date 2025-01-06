package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.*;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.repository.PaymentDAO;
import com.bancos.cuentasbancarias.service.CreditService;
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
public class PaymentServiceImplTest {
    @Mock
    private PaymentDAO paymentDAO;

    @Mock
    private CreditDAO creditDAO;

    @Mock
    private CreditService creditService;

    @Mock
    private MovementDAO movementDAO;

    @Mock
    private CreditCardDAO creditCardDAO;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private Credit credit;
    private Client client;
    private ClientType clientType;
    private CreditCard creditCard;
    @BeforeEach
    void setUp() {
        payment = new Payment();
        payment.setCreditId(new ObjectId());
        payment.setAmount(100.0);

        CreditType creditType = new CreditType();
        creditType.setNombreCredito("TARJETA_CREDITO");

        clientType=new ClientType();
        clientType.setId(new ObjectId());
        clientType.setNombre("PERSONA");
        client=new Client();

        client.setId(new ObjectId());
        client.setNombre("Cesar Huanes");
        client.setClientType(clientType);


        credit = new Credit();
        credit.setAmount(500.0);
        credit.setAmountAvailable(200.0);
        credit.setCreditType(creditType);
        credit.setClient(client);
        credit.setClientId(new ObjectId());

        creditCard = new CreditCard();
        creditCard.setAmountAviable(100.0);
    }

    @Test
    void testSavePayment_ValidPayment() {
        when(creditDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(credit));
        when(creditService.saveCredit(any(Credit.class))).thenReturn(Mono.just(credit));
        when(paymentDAO.save(any(Payment.class))).thenReturn(Mono.just(payment));

        Mono<Payment> result = paymentService.savePayment(payment);

        StepVerifier.create(result)
                .expectNext(payment)
                .verifyComplete();
    }

    @Test
    void testSavePayment_ExceedsCreditAmount() {
        payment.setAmount(400.0); // Exceeds available credit

        when(creditDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(credit));

        Mono<Payment> result = paymentService.savePayment(payment);

        StepVerifier.create(result)
                .expectError(ValidationException.class)
                .verify();
    }

    @Test
    void testMakePayment_ValidPayment() {
        ObjectId creditId = new ObjectId();
        double amount = 100.0;
        ObjectId payerId = new ObjectId();

        when(creditDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(credit));
        when(creditDAO.save(any(Credit.class))).thenReturn(Mono.just(credit));
        when(movementDAO.save(any(Movement.class))).thenReturn(Mono.just(new Movement()));
        when(paymentDAO.save(any(Payment.class))).thenReturn(Mono.just(payment));
        when(creditCardDAO.findByClientId(any(ObjectId.class))).thenReturn(Flux.just(creditCard));
        when(creditCardDAO.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));

        Mono<Credit> result = paymentService.makePayment(creditId, amount, payerId);

        StepVerifier.create(result)
                .expectNext(credit)
                .verifyComplete();
    }
}
