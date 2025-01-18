package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.TypeMovement;
import com.bancos.cuentasbancarias.documents.TypeProduct;
import com.bancos.cuentasbancarias.dto.MovementDto;
import com.bancos.cuentasbancarias.documents.Movement;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.service.AccountService;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.MovementService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class MovementServiceImpl implements MovementService {
    private static final Logger logger = LoggerFactory.getLogger(MovementServiceImpl.class);


    private final MovementDAO movementDAO;
    private final AccountDAO accountDAO;
    private final CreditDAO creditDAO;
    private final AccountService accountService;
    private final CreditService creditService;
    @Override
    public Mono<Movement> saveMovement(Movement movement) {
        if (movement.getTypeProduct() == TypeProduct.ACCOUNT) {
            logger.info("MovementServiceImpl.saveMovement.tipoProducto={}", movement.getTypeProduct());
            return accountDAO.findById(movement.getProductoId())
                    .flatMap(account -> {
                        if (movement.getTypeMovement() == TypeMovement.DEPOSITO) {
                            account.setSaldo(account.getSaldo() + movement.getAmount());
                        } else if (movement.getTypeMovement() == TypeMovement.RETIRO) {
                            if (movement.getAmount() > account.getSaldo()) {
                                return Mono.error(new ValidationException("El monto retirado excede el monto disponible."));
                            }
                            account.setSaldo(account.getSaldo() - movement.getAmount());
                        }
                        logger.info("MovementServiceImpl.saveMovement.nuevoMonto={}", account.getSaldo());
                        // Actualizamos el monto en la tabla cuenta
                        return accountService.updateCuenta(account.getId().toString(), account).then(Mono.just(account));
                    })
                    .flatMap(account -> {
                        // Asigna la cuenta actualizada al movimiento
                        movement.setAccount(account);
                        // Guardamos el movimiento
                        return movementDAO.save(movement);
                    });
        } else if (movement.getTypeProduct() == TypeProduct.CREDIT) {
            logger.info("MovementServiceImpl.saveMovement.tipoProducto={}", movement.getTypeProduct());
            return creditDAO.findById(movement.getProductoId())
                    .flatMap(credit -> {
                        if (movement.getTypeMovement() == TypeMovement.DEPOSITO) {
                            credit.setAmountAvailable(credit.getAmountAvailable() + movement.getAmount());
                        } else if (movement.getTypeMovement() == TypeMovement.RETIRO) {
                            if (movement.getAmount() > credit.getAmountAvailable()) {
                                return Mono.error(new ValidationException("El monto retirado excede el monto disponible."));
                            }
                            credit.setAmountAvailable(credit.getAmountAvailable() - movement.getAmount());
                        }
                        logger.info("MovementServiceImpl.saveMovement.nuevoMonto={}", credit.getAmountAvailable());
                        // Actualizamos el monto en la tabla crédito
                        return creditService.updateCredit(credit.getId().toString(),credit).then(Mono.just(credit));
                    })
                    .flatMap(credit -> {
                        // Asigna el crédito actualizado al movimiento
                        movement.setCredit(credit);
                        // Guardamos el movimiento
                        return movementDAO.save(movement);
                    });
        } else {
            return Mono.error(new ValidationException("Tipo de producto no soportado"));
        }
    }

}
