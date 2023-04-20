package com.scurity.userProj.app.user.service;

import com.scurity.userProj.app.exception.CustomException;
import com.scurity.userProj.app.exception.ErrorResult;
import com.scurity.userProj.app.user.model.UserDetailsImpl;
import com.scurity.userProj.app.user.model.UserEntity;
import com.scurity.userProj.app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Optional<UserEntity> optionalUser = userRepository.findByUserId(username);
        UserEntity user = optionalUser.orElseThrow(() -> new CustomException(ErrorResult.USER_NOT_FOUND));
        return UserDetailsImpl.builder()
                .user(user)
                .authorities(setAuthority(user))
                .build();
    }
    public Collection<? extends GrantedAuthority> setAuthority(UserEntity user){
       return List.of(new SimpleGrantedAuthority(user.getRole().getValue()));
    }
}
