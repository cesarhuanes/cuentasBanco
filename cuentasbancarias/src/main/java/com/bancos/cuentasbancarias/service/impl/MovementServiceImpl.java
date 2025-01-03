package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.dto.MovementDTO;
import com.bancos.cuentasbancarias.documents.Movement;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.service.AccountService;
import com.bancos.cuentasbancarias.service.MovementService;
import com.bancos.cuentasbancarias.util.Constants;
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
    private final AccountService accountService;

    @Override
    public Mono<Movement> saveMovement(Movement movement) {
        return accountDAO.findById(movement.getAccountId())
                .flatMap(account -> {
                    if(movement.getTypeMovement().name() == Constants.DEPOSITO) {
                        account.setSaldo(account.getSaldo() + movement.getAmount());
                    } else if (movement.getTypeMovement().name() == Constants.RETIRO) {
                        account.setSaldo(account.getSaldo() - movement.getAmount());
                    }
                    logger.info("MovementServiceImpl.saveMovement.nuevoMonto={}",account.getSaldo());
                    //actualizamos el monto en la tabla cuenta
                    return accountService.updateCuenta(account.getId().toString(),account).then(Mono.just(account));
                })
                .flatMap(account -> {
                    // Asigna la cuenta actualizada al movimiento
                    movement.setAccount(account);
                    //guardamos el movimiento
                    return movementDAO.save(movement);
                });
    }
    @Override
    public Flux<MovementDTO> getMovementsByAccountId(String accountId) {
        ObjectId accountID=new ObjectId(accountId);
        return movementDAO.findByAccountId(accountID)
                .map(movement -> {
                    MovementDTO dto = new MovementDTO();
                    dto.setAmount(movement.getAmount());
                    dto.setTypeMovement(movement.getTypeMovement());
                    return dto;
                });
    }
}
