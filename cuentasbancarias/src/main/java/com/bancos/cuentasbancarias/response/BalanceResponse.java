package com.bancos.cuentasbancarias.response;

import com.bancos.cuentasbancarias.dto.BalanceDto;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class BalanceResponse {
    private ObjectId clientId;
    private String nameClient;
    private String typeClient;
    private List<BalanceDto> lstBalanceDtoList;

}
