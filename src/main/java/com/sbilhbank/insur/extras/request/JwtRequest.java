package com.sbilhbank.insur.extras.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class JwtRequest implements Serializable {
    private static final String DOMAIN = "@sbilhbank.com.kh";
    @NotNull
    private String username;
    @NotNull
    private String password;
    public void setUsername(String username){
        this.username = username;
    }
}