package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.AccountType;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.repository.AccountTypeDAO;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.ClientTypeDAO;
import com.bancos.cuentasbancarias.service.ClientService;
import com.bancos.cuentasbancarias.util.Constants;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
   private ClientDAO clientDAO;
   @Autowired
   private AccountDAO accountDAO;
   @Autowired
   private ClientTypeDAO clientTypeDAO;
   @Autowired
   private AccountTypeDAO accountTypeDAO;
    @Override
    public Flux<Client> findAll() {
        return clientDAO.findAll()
                .flatMap(client -> clientTypeDAO.findById(client.getClientTypeId())
                        .map(clientType -> {
                            client.setClientType(clientType);
                            return client;
                        }));
    }

    @Override
    public Mono<Client> findById(String id) {
        ObjectId clienteId=new ObjectId(id);
        return clientDAO.findById(clienteId)
                .flatMap(client -> clientTypeDAO.findById(client.getClientTypeId())
                        .map(clientType -> {
                            client.setClientType(clientType);
                            return client;
                        }));
    }

    @Override
    public Mono<Client> save(Client client) {
        return clientTypeDAO.findById(client.getClientTypeId())
                .flatMap(clientType -> {
                    client.setClientType(clientType);
                    return clientDAO.save(client);
                });
    }

    @Override
    public Mono<Void> deleteById(String id) {
        ObjectId clienteId=new ObjectId(id);
        return clientDAO.deleteById(clienteId);
    }

    @Override
    public Mono<List<Account>> saveCuentaByCliente(String clienteId, List<Account> lstAccounts) {
       ObjectId objectId=new ObjectId(clienteId);
        return clientDAO.findById(objectId)
                .flatMap(cliente -> {
                    // Asignar el Client a cada Account
                    Flux<Account> accountsWithClient = Flux.fromIterable(lstAccounts)
                            .doOnNext(account -> account.setClient(cliente));

                    // Asignar el AccountType a cada Account
                    Flux<Account> accountsWithClientAndType = accountsWithClient.flatMap(account ->
                            accountTypeDAO.findById(account.getAccountTypeId())
                                    .map(accountType -> {
                                        account.setAccountType(accountType);
                                        return account;
                                    })
                    );

                    return accountsWithClientAndType.collectList().flatMap(accounts -> {
                        String tipoClienteNombre = cliente.getClientType().getNombre();
                        logger.info("Tipo Cliente = {}", tipoClienteNombre);

                        Mono<Void> validationMono;
                        if (tipoClienteNombre.equals(Constants.TIPO_CLIENTE_PERSONA)) {
                            logger.info("Validando Cliente Persona = {}");
                            validationMono = validarCuentasPersonales(cliente, accounts);
                        } else if (tipoClienteNombre.equals(Constants.TIPO_CLIENTE_EMPRESA)) {
                            logger.info("Validando Cliente Empresa = {}");
                            validationMono = validarCuentasEmpresariales(cliente, accounts);
                        } else {
                            validationMono = Mono.empty();
                        }

                        return validationMono.then(guardarCuentas(cliente, accounts, objectId));
                    });
                });


    }

    private Mono<Void> validarCuentasPersonales(Client client, List<Account> lstAccounts) {
        long ahorroCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .map((AccountType::getNombre))
                        .block()
                        .equals(Constants.CUENTA_AHORRO))
                .count();
        long corrienteCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .map((AccountType::getNombre))
                        .block()
                        .equals(Constants.CUENTA_CORRIENTE))
                .count();
        long plazoFijoCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .map((AccountType::getNombre))
                        .block()
                        .equals(Constants.PLAZO_FIJO))
                .count();

        // Incluir las cuentas del parámetro lstAccounts en el conteo
        for (Account cuenta : lstAccounts) {
            switch (cuenta.getAccountType().getNombre()) {
                case Constants.CUENTA_AHORRO:
                    ahorroCount++;
                    break;
                case Constants.CUENTA_CORRIENTE:
                    corrienteCount++;
                    break;
                case Constants.PLAZO_FIJO:
                    plazoFijoCount++;
                    break;
            }
        }
        logger.info("Cantidad de cuentas de ahorro ={} ",ahorroCount);
        logger.info("Cantidad de cuentas de corriente ={} ",corrienteCount);
        logger.info("Cantidad de cuentas de plazo fijo = {} ",plazoFijoCount);
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
                        .map((AccountType::getNombre))
                        .block()
                        .equals(Constants.CUENTA_AHORRO))
                .count();
        long plazoFijoCount = client.getCuentas().stream()
                .filter(cuentaId -> accountDAO.findById(cuentaId)
                        .map(Account::getAccountType)
                        .map((AccountType::getNombre))
                        .block()
                        .equals(Constants.PLAZO_FIJO))
                .count();

        // Validar que no se creen cuentas de ahorro o plazo fijo para clientes empresariales
        for (Account cuenta : lstAccounts) {

            String tipoCuentaNombre = cuenta.getAccountType().getNombre();
            if (tipoCuentaNombre.equals(Constants.CUENTA_AHORRO)) {
                ahorroCount++;
            }
            if (tipoCuentaNombre.equals(Constants.PLAZO_FIJO)) {
                plazoFijoCount++;
            }
            // Verificar que cada cuenta corriente tenga al menos un titular
            if (tipoCuentaNombre.equals(Constants.CUENTA_CORRIENTE)) {
                if (cuenta.getTitulares() == null || cuenta.getTitulares().isEmpty()) {
                    return Mono.error(new IllegalArgumentException("Una cuenta empresarial debe tener al menos un titular."));
                }
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
    logger.info("Guardando cuentas ={}");
        lstAccounts.forEach(cuenta -> {
        cuenta.setClienteId(objectId);
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
