package com.dart.user.config;

import com.dart.common.service.auth.UserDetailsServiceImpl;
import com.dart.common.service.auth.session.GaeSessionRepository;
import com.dart.data.domain.Session;
import com.dart.data.factory.SessionFactory;
import com.dart.data.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * @author RMPader
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionFactory sessionFactory;

    @Bean
    public org.springframework.session.SessionRepository<Session> sessionRepository() {
        return new GaeSessionRepository(sessionRepository, sessionFactory);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SessionRepositoryFilter<Session> sessionRepositoryFilter = new SessionRepositoryFilter<>(sessionRepository());
        sessionRepositoryFilter.setHttpSessionStrategy(new CookieHttpSessionStrategy());
        // @formatter:off
        http.httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/registration/*").permitAll()
                .anyRequest().authenticated()
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            .and()
                .addFilterBefore(sessionRepositoryFilter, ChannelProcessingFilter.class);;
        // @formatter:on
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder,
                                UserDetailsServiceImpl userDetailsService,
                                PasswordEncoder passwordEncoder,
                                MessageSource messageSource) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setMessageSource(messageSource);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        authenticationManagerBuilder.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
