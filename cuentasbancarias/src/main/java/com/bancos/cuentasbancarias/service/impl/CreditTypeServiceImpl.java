package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.documents.CreditType;
import com.bancos.cuentasbancarias.repository.CreditTypeDAO;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.CreditTypeService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class CreditTypeServiceImpl implements CreditTypeService {

    private final CreditTypeDAO creditTypeDAO;

    @Override
    public Mono<CreditType> saveCreditType(CreditType creditType) {
        return creditTypeDAO.save(creditType);
    }
}
