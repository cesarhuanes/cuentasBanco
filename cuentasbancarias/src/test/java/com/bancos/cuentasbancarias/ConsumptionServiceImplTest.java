package com.bancos.cuentasbancarias;
import com.bancos.cuentasbancarias.documents.Consumption;
import com.bancos.cuentasbancarias.documents.CreditCard;
import com.bancos.cuentasbancarias.repository.ConsumptionDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.service.impl.ConsumptionServiceImpl;
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
public class ConsumptionServiceImplTest {
    @Mock
    private CreditCardDAO creditCardDAO;

    @Mock
    private ConsumptionDAO consumptionDAO;

    @InjectMocks
    private ConsumptionServiceImpl consumptionService;

    private Consumption consumption;
    private CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard();
        creditCard.setId(new ObjectId("677bfea0585e235b1c201683"));
        creditCard.setLimitCredit(500.0);
        creditCard.setAmountAviable(200.0);

        consumption = new Consumption();
        consumption.setCreditCardId(creditCard.getId());
        consumption.setAmountConsumption(100.0);
    }

    @Test
    void testSaveConsumption_ValidConsumption() {
        when(creditCardDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(creditCard));
        when(creditCardDAO.save(any(CreditCard.class))).thenReturn(Mono.just(creditCard));
        when(consumptionDAO.save(any(Consumption.class))).thenReturn(Mono.just(consumption));

        Mono<Consumption> result = consumptionService.saveConsumption(consumption);

        StepVerifier.create(result)
                .expectNext(consumption)
                .verifyComplete();
    }

    @Test
    void testSaveConsumption_CreditCardNotFound() {
        when(creditCardDAO.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        Mono<Consumption> result = consumptionService.saveConsumption(consumption);

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException && throwable.getMessage().equals("Credit card not found"))
                .verify();
    }
}
