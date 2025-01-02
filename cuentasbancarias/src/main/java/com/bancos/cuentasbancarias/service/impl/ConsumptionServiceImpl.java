package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Consumption;
import com.bancos.cuentasbancarias.repository.ConsumptionDAO;
import com.bancos.cuentasbancarias.repository.CreditCardDAO;
import com.bancos.cuentasbancarias.service.ConsumptionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class ConsumptionServiceImpl implements ConsumptionService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumptionServiceImpl.class);

    private final CreditCardDAO creditCardDAO;
    private final ConsumptionDAO consumptionDAO;

    @Override
    public Mono<Consumption> saveConsumption(Consumption consumption) {
        return creditCardDAO.findById(consumption.getCreditCardId())
                .flatMap(creditCard -> {
                    logger.info("ConsumptionServiceImpl.saveConsumption [monto disponible antes de consumo] ={}",creditCard.getAmountAviable());
                    creditCard.setAmountAviable(creditCard.getLimitCredit()-consumption.getAmountConsumption());
                    logger.info("ConsumptionServiceImpl.saveConsumption [consumo] ={}",consumption.getAmountConsumption());
                    logger.info("ConsumptionServiceImpl.saveConsumption [monto disponible despúes de consumo] ={}",creditCard.getAmountAviable());

                    consumption.setCreditCard(creditCard);
                    return consumptionDAO.save(consumption);
                });
    }
}