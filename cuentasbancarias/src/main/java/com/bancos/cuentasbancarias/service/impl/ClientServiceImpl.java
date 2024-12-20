package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.service.ClientService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
   @Autowired
   private ClientDAO clientDAO;
   @Autowired
   private AccountDAO accountDAO;
    @Override
    public Flux<Client> findAll() {
        return clientDAO.findAll();
    }

    @Override
    public Mono<Client> findById(String id) {
        ObjectId clienteId=new ObjectId(id);
        return clientDAO.findById(clienteId);
    }

    @Override
    public Mono<Client> save(Client client) {
        return clientDAO.save(client);
    }

    @Override
    public Mono<Void> delete(Client client) {
        return clientDAO.delete(client);
    }

    @Override
    public Mono<List<Account>> saveCuentaByCliente(String clienteId, List<Account> lstAccounts) {
        ObjectId objectId=new ObjectId(clienteId);
        return  clientDAO.findById(objectId)
                .flatMap(cliente -> {
                    if (cliente.getCuentas() == null) {
                        cliente.setCuentas(new ArrayList<>()); // Inicializar la lista si es null
                    }

                    lstAccounts.forEach(cuenta -> {
                        cuenta.setCliente_id(objectId);
                        cuenta.setAccountType(cuenta.getAccountType());
                        cuenta.setClient(cliente);
                       });
                    return accountDAO.saveAll(lstAccounts).collectList()
                            .flatMap(savedCuentas -> {
                                // Convertir las cuentas guardadas a ObjectId y añadirlas a la lista de cuentas del cliente
                                List<ObjectId> savedCuentasIds = savedCuentas.stream()
                                        .map(Account::getId)
                                        .collect(Collectors.toList());
                                cliente.getCuentas().addAll(savedCuentasIds);
                                return clientDAO.save(cliente).thenReturn(savedCuentas);
                            });
                });

    }


}
