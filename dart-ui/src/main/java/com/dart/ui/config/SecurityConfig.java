package com.dart.ui.config;

import com.dart.common.service.auth.RegisteringProvidedAuthenticationFilter;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.auth.UserDetailsServiceImpl;
import com.dart.common.service.auth.facebook.Facebook;
import com.dart.common.service.auth.google.Google;
import com.dart.common.service.auth.session.GaeSessionRepository;
import com.dart.common.service.property.PropertiesProvider;
import com.dart.data.domain.Session;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.SessionFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.SessionRepository;
import com.dart.data.repository.UserRepository;
import com.dart.ui.auth.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
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

    @Autowired
    @Facebook
    private TokenVerificationService facebookVerificationService;

    @Autowired
    @Google
    private TokenVerificationService googleVerificationService;

    @Autowired
    private IdentityFactory identityFactory;

    @Autowired
    private IdentityRepository identityRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PropertiesProvider propertiesProvider;

    @Bean
    public org.springframework.session.SessionRepository<Session> sessionRepository() {
        return new GaeSessionRepository(sessionRepository, sessionFactory);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationHandler handler = new AuthenticationHandler();

        RegisteringProvidedAuthenticationFilter googleAuth = new RegisteringProvidedAuthenticationFilter(
                "/auth/google",
                "google",
                googleVerificationService,
                identityRepository,
                userRepository,
                userFactory,
                identityFactory);
        googleAuth.setAuthenticationSuccessHandler(handler);

        RegisteringProvidedAuthenticationFilter facebookAuth = new RegisteringProvidedAuthenticationFilter(
                "/auth/facebook",
                "facebook",
                facebookVerificationService,
                identityRepository,
                userRepository,
                userFactory,
                identityFactory);
        googleAuth.setAuthenticationSuccessHandler(handler);


        SessionRepositoryFilter<Session> sessionRepositoryFilter = new SessionRepositoryFilter<>(sessionRepository());
        sessionRepositoryFilter.setHttpSessionStrategy(new CookieHttpSessionStrategy());
        // @formatter:off
        http.httpBasic().disable()
                .authorizeRequests()
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .successHandler(handler)
                    .failureHandler(handler)
            .and()
                .logout()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new Http403ForbiddenEntryPoint())
            .and()
                .addFilterBefore(sessionRepositoryFilter, ChannelProcessingFilter.class)
                .addFilterBefore(googleAuth,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(facebookAuth,UsernamePasswordAuthenticationFilter.class);
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
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainName("."+propertiesProvider.getDomain());
        return serializer;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
