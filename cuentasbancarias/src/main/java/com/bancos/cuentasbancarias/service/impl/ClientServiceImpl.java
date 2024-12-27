package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.ClientType;
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


                    if (ClientType.PERSONAL==cliente.getClientType()) {
                        return validarCuentasPersonales(cliente, lstAccounts)
                                .then(Mono.defer(() -> guardarCuentas(cliente, lstAccounts, objectId)));
                    } else if(ClientType.EMPRESARIAL==cliente.getClientType()){
                        return validarCuentasEmpresariales(cliente, lstAccounts)
                                .then(Mono.defer(() -> guardarCuentas(cliente, lstAccounts, objectId)));
                    }
                    else {
                        return guardarCuentas(cliente, lstAccounts, objectId);
                    }
                });

    }

    private Mono<Void> validarCuentasPersonales(Client client, List<Account> lstAccounts) {
        long ahorroCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .block()
                        .equals(AccountType.AHORRO))
                .count();
        long corrienteCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .block()
                        .equals(AccountType.CORRIENTE))
                .count();
        long plazoFijoCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .block()
                        .equals(AccountType.PLAZO_FIJO))
                .count();

        // Incluir las cuentas del parámetro lstAccounts en el conteo
        for (Account cuenta : lstAccounts) {
            switch (cuenta.getAccountType()) {
                case AHORRO:
                    ahorroCount++;
                    break;
                case CORRIENTE:
                    corrienteCount++;
                    break;
                case PLAZO_FIJO:
                    plazoFijoCount++;
                    break;
            }
        }

        // Validar que no se exceda el límite de una cuenta por tipo
        if (ahorroCount > 1) {
            return Mono.error(new IllegalArgumentException("El cliente ya tiene una cuenta de ahorro."));
        }
        if (corrienteCount > 1) {
            return Mono.error(new IllegalArgumentException("El cliente ya tiene una cuenta corriente."));
        }
        if (plazoFijoCount > 1) {
            return Mono.error(new IllegalArgumentException("El cliente ya tiene una cuenta de plazo fijo."));
        }

        return Mono.empty();
    }
    private Mono<Void> validarCuentasEmpresariales(Client client, List<Account> lstAccounts) {
        // Contar las cuentas existentes por tipo
        long ahorroCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .block()
                        .equals(AccountType.AHORRO))
                .count();
        long plazoFijoCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .block()
                        .equals(AccountType.PLAZO_FIJO))
                .count();

        // Validar que no se creen cuentas de ahorro o plazo fijo para clientes empresariales
        for (Account cuenta : lstAccounts) {
            if (cuenta.getAccountType() == AccountType.AHORRO) {
                ahorroCount++;
            }
            if (cuenta.getAccountType() == AccountType.PLAZO_FIJO) {
                plazoFijoCount++;
            }
        }

        if (ahorroCount > 0) {
            return Mono.error(new IllegalArgumentException("Un cliente empresarial no puede tener una cuenta de ahorro."));
        }
        if (plazoFijoCount > 0) {
            return Mono.error(new IllegalArgumentException("Un cliente empresarial no puede tener una cuenta de plazo fijo."));
        }

        return Mono.empty();
    }


private Mono<List<Account>> guardarCuentas(Client client, List<Account> lstAccounts, ObjectId objectId) {
    lstAccounts.forEach(cuenta -> {
        cuenta.setCliente_id(objectId);
        cuenta.setAccountType(cuenta.getAccountType());
        cuenta.setClient(client);
    });

    return accountDAO.saveAll(lstAccounts).collectList()
            .flatMap(savedCuentas -> {
                // Convertir las cuentas guardadas a ObjectId y añadirlas a la lista de cuentas del cliente
                List<ObjectId> savedCuentasIds = savedCuentas.stream()
                        .map(Account::getId)
                        .collect(Collectors.toList());
                client.getCuentas().addAll(savedCuentasIds);
                return clientDAO.save(client).thenReturn(savedCuentas);
            });


    }
}
