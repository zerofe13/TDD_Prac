package com.scurity.userProj.app.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserDetailsImpl implements UserDetails {
    @Delegate
    private final UserEntity user;
    private final Collection<? extends GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswd();
    }

    @Override
    public String getUsername() {
        return user.getUserId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getIsEnable();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsEnable();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsEnable();
    }

    @Override
    public boolean isEnabled() {
        return user.getIsEnable();
    }
}
