package com.bancos.cuentasbancarias.service.impl;

import com.bancos.cuentasbancarias.documents.Person;
import com.bancos.cuentasbancarias.documents.Signature;
import com.bancos.cuentasbancarias.repository.PersonDAO;
import com.bancos.cuentasbancarias.repository.SignatureDAO;
import com.bancos.cuentasbancarias.service.SignatureService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class SignatureServiceImpl implements SignatureService {
    private static final Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);

    private final SignatureDAO signatureDAO;
    private final PersonDAO personDAO;
    @Override
    public Mono<Signature> saveSignature(Signature signature) {
        logger.info("SignatureServiceImpl.saveSignature= {}",signature);
        //primero guardamos persona
        return personDAO.save(signature).flatMap(savedPerson-> {
            //luego guardamos holder
            return signatureDAO.save(signature);
        });
    }
}
