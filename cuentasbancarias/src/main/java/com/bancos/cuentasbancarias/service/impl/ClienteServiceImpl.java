package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Cliente;
import com.bancos.cuentasbancarias.documents.Cuenta;
import com.bancos.cuentasbancarias.repository.ClienteDAO;
import com.bancos.cuentasbancarias.repository.CuentaDAO;
import com.bancos.cuentasbancarias.service.ClienteService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements  ClienteService {
   @Autowired
   private ClienteDAO clienteDAO;
   @Autowired
   private CuentaDAO cuentaDAO;
    @Override
    public Flux<Cliente> findAll() {
        return clienteDAO.findAll();
    }

    @Override
    public Mono<Cliente> findById(String id) {
        ObjectId clienteId=new ObjectId(id);
        return clienteDAO.findById(clienteId);
    }

    @Override
    public Mono<Cliente> save(Cliente cliente) {
        return clienteDAO.save(cliente);
    }

    @Override
    public Mono<Void> delete(Cliente cliente) {
        return clienteDAO.delete(cliente);
    }

    @Override
    public Mono<List<Cuenta>> saveCuentaByCliente(String clienteId, List<Cuenta> lstCuentas) {
        ObjectId objectId=new ObjectId(clienteId);
        return  clienteDAO.findById(objectId)
                .flatMap(cliente -> {
                    if (cliente.getCuentas() == null) {
                        cliente.setCuentas(new ArrayList<>()); // Inicializar la lista si es null
                    }

                    lstCuentas.forEach(cuenta -> {
                        cuenta.setCliente_id(objectId);
                        cuenta.setAccountType(cuenta.getAccountType());
                        cuenta.setCliente(cliente);
                       });
                    return cuentaDAO.saveAll(lstCuentas).collectList()
                            .flatMap(savedCuentas -> {
                                // Convertir las cuentas guardadas a ObjectId y añadirlas a la lista de cuentas del cliente
                                List<ObjectId> savedCuentasIds = savedCuentas.stream()
                                        .map(Cuenta::getId)
                                        .collect(Collectors.toList());
                                cliente.getCuentas().addAll(savedCuentasIds);
                                return clienteDAO.save(cliente).thenReturn(savedCuentas);
                            });
                });

    }


}
