package com.dart.common.service.auth;

import com.dart.data.domain.Identity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by rpader on 4/28/16.
 */
public class UserDetailsImpl implements UserDetails {

    private Identity identity;

    public UserDetailsImpl(Identity identity) {
        this.identity = identity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return identity.getProvidedIdentity();
    }

    @Override
    public String getUsername() {
        return identity.getUser()
                       .getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        //TODO: add support for this
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //TODO: add support for this
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //TODO: add support for this
        return true;
    }

    @Override
    public boolean isEnabled() {
        //TODO: add support for this
        return true;
    }
}
