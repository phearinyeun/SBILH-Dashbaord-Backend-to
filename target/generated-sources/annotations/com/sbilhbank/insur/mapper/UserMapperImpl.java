package com.sbilhbank.insur.mapper;

import com.sbilhbank.insur.dto.UserDto;
import com.sbilhbank.insur.entity.primary.Role;
import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.entity.primary.UserLdap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-10T14:34:20+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl extends UserMapper {

    @Override
    public User userDtoToUser(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder<?, ?> user = User.builder();

        user.roles( stringListToRoleSet( userDto.getRoleNames() ) );
        user.id( userDto.getId() );
        user.username( userDto.getUsername() );
        user.password( userDto.getPassword() );
        user.accountLocked( userDto.isAccountLocked() );
        user.enabled( userDto.isEnabled() );
        user.ad( userDto.isAd() );

        return user.build();
    }

    @Override
    public UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder<?, ?> userDto = UserDto.builder();

        userDto.roleNames( roleSetToStringList( user.getRoles() ) );
        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.password( user.getPassword() );
        userDto.accountLocked( user.isAccountLocked() );
        userDto.enabled( user.isEnabled() );
        userDto.ad( user.isAd() );

        return userDto.build();
    }

    @Override
    public UserDto userLdapToUserDto(UserLdap userLdap) {
        if ( userLdap == null ) {
            return null;
        }

        UserDto.UserDtoBuilder<?, ?> userDto = UserDto.builder();

        userDto.roleNames( roleSetToStringList( userLdap.getRoles() ) );
        userDto.id( userLdap.getId() );
        userDto.username( userLdap.getUsername() );
        userDto.accountLocked( userLdap.isAccountLocked() );
        userDto.enabled( userLdap.isEnabled() );
        userDto.department( userLdap.getDepartment() );
        userDto.fullName( userLdap.getFullName() );
        userDto.memberOf( userLdap.getMemberOf() );
        userDto.lastName( userLdap.getLastName() );
        userDto.givenName( userLdap.getGivenName() );
        userDto.mail( userLdap.getMail() );

        return userDto.build();
    }

    @Override
    protected List<String> mapListRoleToListString(List<Role> roles) {
        if ( roles == null ) {
            return null;
        }

        List<String> list = new ArrayList<String>( roles.size() );
        for ( Role role : roles ) {
            list.add( mapRoleToString( role ) );
        }

        return list;
    }

    @Override
    protected List<Role> mapListStringToListRole(List<String> roleNames) {
        if ( roleNames == null ) {
            return null;
        }

        List<Role> list = new ArrayList<Role>( roleNames.size() );
        for ( String string : roleNames ) {
            list.add( mapStringToRole( string ) );
        }

        return list;
    }

    protected Set<Role> stringListToRoleSet(List<String> list) {
        if ( list == null ) {
            return null;
        }

        Set<Role> set = new LinkedHashSet<Role>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( String string : list ) {
            set.add( mapStringToRole( string ) );
        }

        return set;
    }

    protected List<String> roleSetToStringList(Set<Role> set) {
        if ( set == null ) {
            return null;
        }

        List<String> list = new ArrayList<String>( set.size() );
        for ( Role role : set ) {
            list.add( mapRoleToString( role ) );
        }

        return list;
    }
}
