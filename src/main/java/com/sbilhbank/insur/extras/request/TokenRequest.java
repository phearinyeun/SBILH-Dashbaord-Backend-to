package com.sbilhbank.insur.extras.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class TokenRequest {
    @NotEmpty
    private String token;
}
