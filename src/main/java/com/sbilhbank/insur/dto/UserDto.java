package com.sbilhbank.insur.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.validation.compare.Compare;
import com.sbilhbank.insur.validation.requiredif.RequiredIf;
import com.sbilhbank.insur.validation.unique.UniqueKey;
import com.sbilhbank.insur.validation.user.UsernameValidateOnAdField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@UsernameValidateOnAdField(groups = {Create.class, Update.class}, field = "ad")
@UniqueKey(groups = {Create.class, Update.class}, message = "The username is exist.",className = User.class, classMain = User.class, columnName = "username")
@RequiredIf(groups = {Create.class, Update.class}, message = "The password is required.", field = "ad", verifyField = "password")
@Compare(groups = {Create.class, Update.class}, message = "The confirm password doesn't match with password.", field = "password", verifyField = "confirmPassword")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private Long id;
    @NotEmpty(groups = { Create.class, Update.class })
    private String username;
    @NotNull(groups = { Create.class, Update.class })
    private List<String> roleNames;
    @JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @JsonProperty( value = "confirmPassword", access = JsonProperty.Access.WRITE_ONLY)
    private String confirmPassword;
    @NotNull
    private boolean accountLocked;
    @NotNull
    private boolean enabled;
    private String department;
    private String fullName;
    private String memberOf;
    private String lastName;
    private String givenName;
    private String mail;
    @JsonProperty("isAd")
    @NotNull
    private boolean ad;
}
