package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Holder;
import com.bancos.cuentasbancarias.repository.HolderDAO;
import com.bancos.cuentasbancarias.repository.PersonDAO;
import com.bancos.cuentasbancarias.service.HolderService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Service
public class HolderServiceImpl implements HolderService {
    private static final Logger logger = LoggerFactory.getLogger(HolderServiceImpl.class);

    private final HolderDAO holderDAO;
    private final PersonDAO personDAO;
    @Override
    public Mono<Holder> saveHolder(Holder holder) {
        logger.info("HolderServiceImpl.saveHolder= {}",holder);
        //primero guardamos persona
        return personDAO.save(holder).flatMap(savedPerson->{
            //luego guardamos holder
           return holderDAO.save(holder);
        });
    }
}
