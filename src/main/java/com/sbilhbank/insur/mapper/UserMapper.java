package com.sbilhbank.insur.mapper;

import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.entity.primary.UserLdap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.context.annotation.Configuration;

import javax.naming.Name;
import java.util.List;

@Mapper(componentModel = "spring")
@Configuration
public abstract class UserMapper {
    @Mappings({
            @Mapping(source = "roleNames", target = "roles")
    })
    public abstract User userDtoToUser(UserDto userDto);
    @Mappings({
            @Mapping(source = "roles", target = "roleNames")
    })
    public abstract UserDto userToUserDto(User user);

    @Mappings({
            @Mapping(source = "roles", target = "roleNames")
    })
    public abstract UserDto userLdapToUserDto(UserLdap userLdap);
    protected abstract List<String> mapListRoleToListString(List<Role> roles);
    protected String mapRoleToString(Role role){
        return role.getName();
    }

    protected abstract List<Role> mapListStringToListRole(List<String> roleNames);
    protected Role mapStringToRole(String roleName){
        return Role.builder().name(roleName).build();
    }
}
