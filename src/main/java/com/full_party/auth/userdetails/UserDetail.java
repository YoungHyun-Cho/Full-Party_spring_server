package com.full_party.auth.userdetails;

import com.full_party.auth.utils.CustomAuthorityUtils;
import com.full_party.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetail extends User implements UserDetails {

    private final CustomAuthorityUtils customAuthorityUtils;

    UserDetail(User user, CustomAuthorityUtils customAuthorityUtils) {

        this.customAuthorityUtils = customAuthorityUtils;

        setId(user.getId());
        setEmail(user.getEmail());
//            setPassword(passwordEncoder.encode(user.getPassword()));
        setPassword(user.getPassword());
        setRoles(user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customAuthorityUtils.createAuthorities(this.getRoles());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}