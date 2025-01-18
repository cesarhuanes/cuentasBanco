package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Client;
import com.bancos.cuentasbancarias.dto.BalanceDto;
import com.bancos.cuentasbancarias.repository.AccountDAO;
import com.bancos.cuentasbancarias.repository.CreditDAO;
import com.bancos.cuentasbancarias.response.BalanceResponse;
import com.bancos.cuentasbancarias.service.ClientService;
import com.bancos.cuentasbancarias.service.CreditService;
import com.bancos.cuentasbancarias.service.ReportService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ClientService clientService;
    private final CreditDAO creditDAO;
    private final AccountDAO accountDAO;

    @Override
    public Mono<BalanceResponse> getAccountBalance(ObjectId clientId) {
      Mono<Client> client=clientService.findById(clientId.toHexString())
       .switchIfEmpty(Mono.error(new ValidationException("Cliente no encontrado..!")));
// Buscado creditos
      Mono<List<BalanceDto>> lstCredit=creditDAO.findByClientId(clientId)
              .map(credit -> {
                  BalanceDto balanceDto=new BalanceDto();
                  balanceDto.setAccountId(credit.getId());
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
                  balanceDto.setAccountId(account.getId());
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
                     balanceResponse.setClientId(client1.getId());
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

}
