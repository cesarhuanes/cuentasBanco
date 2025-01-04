package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.*;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.repository.PaymentDAO;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.PaymentService;
import com.bancos.cuentasbancarias.util.Constants;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final CreditDAO creditDAO;
    private final CreditService creditService;
    private final MovementDAO movementDAO;
    private final CreditCardDAO creditCardDAO;
    @Override
    public Mono<Payment> savePayment(Payment payment) {
        return creditDAO.findById(payment.getCreditId())
                .flatMap(credit -> {
                    //se puede poner logica de negocio
                    if(credit.getAmountAvailable()+payment.getAmount()>credit.getAmount()){// si lo que pago es mayor a mi linea de credito
                        return Mono.error(new ValidationException("El monto pagado excede al monto de credito."));

                    }
                    return creditService.saveCredit(credit).then(Mono.just(credit));
                })
                .flatMap(credit -> {
                    payment.setCredit(credit);
                    return paymentDAO.save(payment);
                });
    }

    @Override
    public Mono<Credit> makePayment(ObjectId creditId, double amount, ObjectId payerId) {
        return creditDAO.findById(creditId)
                .flatMap(credit -> {
                    credit.setAmountAvailable(credit.getAmountAvailable() + amount);
                    //1-guardamos el credito
                    return creditDAO.save(credit)
                            .flatMap(savedCredit -> {
                                // Registrar el movimiento del pago
                                Movement movement = new Movement();
                                movement.setProductoId(creditId);
                                movement.setTypeProduct(TypeProduct.CREDIT);
                                movement.setAmount(amount);
                                movement.setDateMovement(LocalDateTime.now());
                                movement.setTypeMovement(TypeMovement.DEPOSITO);
                                movement.setPayerId(payerId); // ID del pagador

                                return movementDAO.save(movement)
                                        .then(Mono.just(savedCredit));
                            })
                            .flatMap(savedCredit -> {
                                // Actualizar la tarjeta de crédito si el tipo de crédito es "TARJETA_CREDITO"
                                if (savedCredit.getCreditType().getNombreCredito().equals(Constants.TARJETA_CREDITO)) {
                                    return creditCardDAO.findByClientId(savedCredit.getClientId())
                                            .flatMap(creditCard -> {
                                                creditCard.setAmountAviable(creditCard.getAmountAviable() + amount);
                                                return creditCardDAO.save(creditCard);
                                            })
                                            .then(Mono.just(savedCredit));
                                } else {
                                    return Mono.just(savedCredit);
                                }
                            })
                            .flatMap(savedCredit->{
                                // Guardar la entidad Payment
                                Payment payment = new Payment();
                                payment.setCreditId(creditId);
                                payment.setAmount(amount);
                                payment.setDatePay(new Date());
                                payment.setCredit(savedCredit);
                                return paymentDAO.save(payment).then(Mono.just(savedCredit));

                            });
                });
    }


}
