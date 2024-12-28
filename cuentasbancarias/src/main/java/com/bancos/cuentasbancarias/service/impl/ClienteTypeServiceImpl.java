package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.ClientType;
import com.bancos.cuentasbancarias.repository.ClientTypeDAO;
import com.bancos.cuentasbancarias.service.ClienteTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Slf4j
@Service
public class ClienteTypeServiceImpl implements ClienteTypeService {
    @Autowired
    private ClientTypeDAO clientTypeDAO;

    @Override
    public Mono<ClientType> save(ClientType clienteType) {
        return clientTypeDAO.save(clienteType);
    }
}
