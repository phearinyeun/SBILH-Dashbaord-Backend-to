package com.sbilhbank.insur.service.log;

import com.sbilhbank.insur.entity.primary.Logging;
import com.sbilhbank.insur.repository.primary.LoggingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoggingService {
    private final LoggingRepository loggingRepository;

    public Logging createLog(Logging logging){
        if(Optional.ofNullable(logging.getException()).orElse("null").length()>5000)
            logging.setException(Optional.ofNullable(logging.getException()).orElse("null").substring(0, 5000));
        if(Optional.ofNullable(logging.getCause()).orElse("null").length()>5000)
            logging.setCause(Optional.ofNullable(logging.getCause()).orElse("null").substring(0, 5000));
        if(Optional.ofNullable(logging.getMessage()).orElse("null").length()>5000)
            logging.setMessage(Optional.ofNullable(logging.getMessage()).orElse("null").substring(0, 5000));
        Logging logging1 = loggingRepository.save(logging);
        return logging1;
    }
}
