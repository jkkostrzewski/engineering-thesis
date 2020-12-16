package com.example.thesis.backend.security;

import com.example.thesis.views.auth.TokenRegistrationView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    public static final String LOGIN_PROCESSING_URL = "/login";
    public static final String LOGIN_FAILURE_URL = "/login?error";
    public static final String LOGIN_URL = "/login";
    public static final String LOGOUT_URL = "/logout";
    public static final String LOGOUT_SUCCESS_URL = "/login";
    public static final String LOGIN_SUCCESS_URL = "/notice-board/Main%20Board"; //space in between

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public CustomRequestCache requestCache() {
        return new CustomRequestCache();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and().authorizeRequests()
                .antMatchers(TokenRegistrationView.ROUTE + "/**").permitAll()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .defaultSuccessUrl(LOGIN_SUCCESS_URL, true)
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL).logoutUrl(LOGOUT_URL).permitAll();
        http.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",
                "/offline.html",
                "/frontend/**",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**",
                "/webjars/**",
                "frontend-es5/**", "/frontend-es6/**"
        );
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
