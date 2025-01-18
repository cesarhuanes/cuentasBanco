package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Account;
import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.documents.Credit;
import com.bancos.cuentasbancarias.dto.BalanceDto;
import com.bancos.cuentasbancarias.dto.MovementDto;
import com.bancos.cuentasbancarias.dto.ProductDto;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.ClientDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.repository.MovementDAO;
import com.bancos.cuentasbancarias.response.BalanceResponse;
import com.bancos.cuentasbancarias.response.MovementResponse;
import com.bancos.cuentasbancarias.service.ClientService;
import com.bancos.cuentasbancarias.service.ReportService;
import com.bancos.cuentasbancarias.util.Util;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ClientService clientService;
    private final CreditDAO creditDAO;
    private final AccountDAO accountDAO;
    private final ClientDAO clientDAO;
    private final MovementDAO movementDAO;

    @Override
    public Mono<BalanceResponse> getAccountBalance(ObjectId clientId) {
      Mono<Client> client=clientService.findById(clientId.toHexString())
       .switchIfEmpty(Mono.error(new ValidationException("Cliente no encontrado..!")));
// Buscado creditos
      Mono<List<BalanceDto>> lstCredit=creditDAO.findByClientId(clientId)
              .map(credit -> {
                  BalanceDto balanceDto=new BalanceDto();
                  balanceDto.setAccountId(Util.convertObjectIdToString(credit.getId()));
                  balanceDto.setBalance(credit.getAmountAvailable());
                  balanceDto.setTypeAccount(credit.getCreditType().getNombreCredito());
                  return  balanceDto;
              }).collectList();
              //.filter(list -> !list.isEmpty())
              //.switchIfEmpty(Mono.error(new ValidationException("Creditos no encontrados")));

//Buscando cuentas
      Mono<List<BalanceDto>> lstAccount=accountDAO.findByClientId(clientId)
              .map(account -> {
                  BalanceDto balanceDto=new BalanceDto();
                  balanceDto.setAccountId(Util.convertObjectIdToString(account.getId()));
                  balanceDto.setBalance(account.getSaldo());
                  balanceDto.setTypeAccount(account.getAccountType().getNombre());
                  return  balanceDto;

              }).collectList();
                //.filter(list -> !list.isEmpty())
                //.switchIfEmpty(Mono.error(new ValidationException("Cuentas no econtradas")));



        return Mono.zip(client,lstAccount,lstCredit)
                .map(objects -> {
                    Client client1=objects.getT1();
                    List<BalanceDto> lstBalanceDtos=objects.getT2();
                    lstBalanceDtos.addAll(objects.getT3());

                    List<BalanceDto> creditCardList = objects.getT2();
                    List<BalanceDto> accountList = objects.getT3();

                    logger.info("ReportServiceImpl.getAccountBalance.creditCardList",creditCardList.size());
                    logger.info("ReportServiceImpl.getAccountBalance.accountList",accountList.size());
                    logger.info("ReportServiceImpl.getAccountBalance.client",client1.getNombre());

                     BalanceResponse balanceResponse=new BalanceResponse();
                     balanceResponse.setClientId(Util.convertObjectIdToString(client1.getId()));
                     balanceResponse.setNameClient(client1.getNombre());
                     balanceResponse.setTypeClient(client1.getClientType().getNombre());
                     balanceResponse.setLstBalanceDtoList(lstBalanceDtos);
                  return  balanceResponse;
                })
                .onErrorResume(e -> {
                    logger.error("Error al recuperar el saldo de la cuenta: {}", e.getMessage());
                    return Mono.error(new ValidationException("Error al recuperar el saldo de la cuenta: " + e.getMessage()));
                });
    }

    @Override
    public Mono<MovementResponse> findAllMovementsByClientId(ObjectId clientId) {
        // Recuperar el cliente
        Mono<Client> clientMono = clientDAO.findById(clientId)
                .switchIfEmpty(Mono.error(new ValidationException("Client not found")));

        // Recuperar todas las cuentas del cliente
        Flux<Account> accounts = accountDAO.findByClientId(clientId);

        // Recuperar todos los créditos del cliente
        Flux<Credit> credits = creditDAO.findByClientId(clientId);

        // Mapear cuentas a ProductDto
        Flux<ProductDto> accountProducts = accounts.flatMap(account ->
                movementDAO.findByProductoId(account.getId())
                        .map(movement -> {
                            MovementDto movementDto = new MovementDto();
                            movementDto.setAmount(movement.getAmount());
                            movementDto.setDateMovement(movement.getDateMovement());
                            movementDto.setTypeMovement(movement.getTypeMovement().toString());
                            return movementDto;
                        })
                        .collectList()
                        .map(movements -> {
                            ProductDto productDto = new ProductDto();
                            productDto.setProductId(Util.convertObjectIdToString(account.getId()));
                            productDto.setProductName(account.getAccountType().getNombre());
                            productDto.setLstMovement(movements);
                            return productDto;
                        })
        );

        // Mapear créditos a ProductDto
        Flux<ProductDto> creditProducts = credits.flatMap(credit ->
                movementDAO.findByProductoId(credit.getId())
                        .map(movement -> {
                            MovementDto movementDto = new MovementDto();
                            movementDto.setAmount(movement.getAmount());
                            movementDto.setDateMovement(movement.getDateMovement());
                            movementDto.setTypeMovement(movement.getTypeMovement().toString());
                            return movementDto;
                        })
                        .collectList()
                        .map(movements -> {
                            ProductDto productDto = new ProductDto();
                            productDto.setProductId(Util.convertObjectIdToString(credit.getId()));
                            productDto.setProductName(credit.getCreditType().getNombreCredito());
                            productDto.setLstMovement(movements);
                            return productDto;
                        })
        );

        // Combinar todos los ProductDto y construir el MovementResponse
        return clientMono.flatMap(client ->
                Flux.concat(accountProducts, creditProducts)
                        .collectList()
                        .map(productDtos -> {
                            MovementResponse movementResponse = new MovementResponse();
                            movementResponse.setClientName(client.getNombre());
                            movementResponse.setLstProduct(productDtos);
                            return movementResponse;
                        })
        );
    }

}
