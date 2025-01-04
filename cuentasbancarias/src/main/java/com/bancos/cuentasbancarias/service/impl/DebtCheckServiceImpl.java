package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.service.DebtCheckService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class DebtCheckServiceImpl implements DebtCheckService {
    private final CreditDAO creditDAO;
    /***R3 2.	Un cliente no podrá adquirir un producto
     * si posee alguna deuda vencida en algún producto de crédito.*/

    @Override
    public Mono<Boolean> hasOverdueDebt(ObjectId clientId) {
        return creditDAO.findByClientId(clientId)
                .filter(Credit::isOverdue)
                .hasElements();
    }
}
