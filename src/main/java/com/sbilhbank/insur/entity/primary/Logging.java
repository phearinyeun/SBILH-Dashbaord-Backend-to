package com.sbilhbank.insur.entity.primary;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "logging")
@Data
@SuperBuilder
@NoArgsConstructor
public class Logging {
    @Id
    private UUID loggingId = UUID.randomUUID();
    private String classPackage;
    private String method;
    private Date logDate;
    @Column(length = 5000)
    private String exception;
    private String logType;
    @Column(length = 5000)
    private String cause;
    @Column(length = 5000)
    private String message;
}
