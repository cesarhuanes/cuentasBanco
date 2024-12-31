package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Payment;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.PaymentDAO;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.PaymentService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO;
    private final CreditDAO creditDAO;
    private final CreditService creditService;
    @Override
    public Mono<Payment> savePayment(Payment payment) {
        return creditDAO.findById(payment.getCreditId())
                .flatMap(credit -> {
                    //se puede poner logica de negocio
                    if(credit.getAmountAviable()+payment.getAmount()>credit.getAmount()){// si lo que pago es mayor a mi linea de credito
                        return Mono.error(new ValidationException("El monto pagado excede al monto de credito."));

                    }
                    return creditService.saveCredit(credit).then(Mono.just(credit));
                })
                .flatMap(credit -> {
                    payment.setCredit(credit);
                    return paymentDAO.save(payment);
                });
    }
}
