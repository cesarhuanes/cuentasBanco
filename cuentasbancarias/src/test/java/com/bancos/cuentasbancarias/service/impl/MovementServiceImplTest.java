package com.bancos.cuentasbancarias.service.impl;
import com.bancos.cuentasbancarias.documents.*;
import com.bancos.cuentasbancarias.dto.MovementDTO;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.service.AccountService;
import com.bancos.cuentasbancarias.service.CreditService;
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
public class MovementServiceImplTest {
    @Mock
    private MovementDAO movementDAO;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private CreditDAO creditDAO;

    @Mock
    private AccountService accountService;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private MovementServiceImpl movementService;

    private Movement movement;

    @BeforeEach
    void setUp() {
        movement = new Movement();
        movement.setId(new ObjectId());
        movement.setTypeProduct(TypeProduct.ACCOUNT);
        movement.setTypeMovement(TypeMovement.DEPOSITO);
        movement.setAmount(100.0);
        movement.setProductoId(new ObjectId());
    }

    @Test
    void testSaveMovement_AccountDeposit() {
        Account account = new Account();
        account.setId(new ObjectId());
        account.setSaldo(100.0);

        when(accountDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(account));
        when(accountService.updateCuenta(any(String.class), any(Account.class))).thenReturn(Mono.empty());
        when(movementDAO.save(any(Movement.class))).thenReturn(Mono.just(movement));

        Mono<Movement> result = movementService.saveMovement(movement);

        StepVerifier.create(result)
                .expectNext(movement)
                .verifyComplete();
    }

    @Test
    void testSaveMovement_CreditDeposit() {
        movement.setTypeProduct(TypeProduct.CREDIT);
        Credit credit = new Credit();
        credit.setAmountAvailable(100.0);
        credit.setId( new ObjectId());
        when(creditDAO.findById(any(ObjectId.class))).thenReturn(Mono.just(credit));
        when(creditService.updateCredit(any(String.class), any(Credit.class))).thenReturn(Mono.empty());
        when(movementDAO.save(any(Movement.class))).thenReturn(Mono.just(movement));

        Mono<Movement> result = movementService.saveMovement(movement);

        StepVerifier.create(result)
                .expectNext(movement)
                .verifyComplete();
    }

    @Test
    void testGetMovementsByAccountId() {
        MovementDTO movement1 = new MovementDTO();
        movement1.setAmount(50.0);
        movement1.setTypeMovement(TypeMovement.DEPOSITO);

        when(movementDAO.findByAccountId(any(ObjectId.class))).thenReturn(Flux.just(movement1));

        Flux<MovementDTO> result = movementService.getMovementsByAccountId("60d5f9b5e1d3c0001c8e4d5a");

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getAmount() == 50.0 && dto.getTypeMovement() == TypeMovement.DEPOSITO)
                .verifyComplete();
    }
}
