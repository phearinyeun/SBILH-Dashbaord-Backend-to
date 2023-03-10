package com.sbilhbank.insur.extras.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class JwtResponse implements Serializable
{
    @JsonProperty("access_token")
    private final String accessToken;
    private Date accessTokenIssueAt;
    private Date accessTokenExpireAt;
    @JsonProperty("refresh_token")
    private final String refreshToken;
    private Date refreshTokenIssueAt;
    private Date refreshTokenExpireAt;
}