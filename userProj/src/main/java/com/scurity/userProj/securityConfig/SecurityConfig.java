package com.scurity.userProj.securityConfig;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configurable
@EnableWebSecurity
public class SecurityConfig {

    private CorsFilter corsFilter;

    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                //UsernamePasswordAuthenticationFilter 전에 corsFilter가 실행
                .addFilterBefore(corsFilter,UsernamePasswordAuthenticationFilter.class)

        ;

        return httpSecurity.build();
    }
}
