package com.sbilhbank.insur.repository.primary;


import com.sbilhbank.insur.entity.primary.Logging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoggingRepository extends JpaRepository<Logging, UUID> {
}
