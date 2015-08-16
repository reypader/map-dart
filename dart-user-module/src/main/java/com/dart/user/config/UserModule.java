package com.dart.user.config;

import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.common.service.auth.JwtHttpRequestAuthorizationService;
import com.dart.common.service.auth.TokenVerificationService;
import com.dart.common.service.auth.facebook.Facebook;
import com.dart.common.service.auth.facebook.FacebookTokenVerificationService;
import com.dart.common.service.auth.google.Google;
import com.dart.common.service.auth.google.GoogleTokenVerificationService;
import com.dart.common.service.auth.google.Recaptcha;
import com.dart.common.service.auth.google.RecaptchaTokenVerificationService;
import com.dart.common.service.mail.GenericMailSenderService;
import com.dart.common.service.mail.MailSenderService;
import com.dart.common.service.properties.FilePropertiesProvider;
import com.dart.common.service.properties.PropertiesProvider;
import com.dart.data.factory.IdentityFactory;
import com.dart.data.factory.RegistrationFactory;
import com.dart.data.factory.UserFactory;
import com.dart.data.objectify.factory.IdentityFactoryImpl;
import com.dart.data.objectify.factory.RegistrationFactoryImpl;
import com.dart.data.objectify.factory.UserFactoryImpl;
import com.dart.data.objectify.repository.IdentityRepositoryImpl;
import com.dart.data.objectify.repository.RegistrationRepositoryImpl;
import com.dart.data.objectify.repository.UserRepositoryImpl;
import com.dart.data.repository.IdentityRepository;
import com.dart.data.repository.RegistrationRepository;
import com.dart.data.repository.UserRepository;
import com.dart.user.service.UserService;
import com.dart.user.service.UserServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.googlecode.objectify.ObjectifyFilter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.inject.Singleton;

public class UserModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectifyFilter.class).in(Singleton.class);

        bind(UserService.class).to(UserServiceImpl.class);

        //Manually provided
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(UserFactory.class).to(UserFactoryImpl.class);
        bind(RegistrationRepository.class).to(RegistrationRepositoryImpl.class);
        bind(RegistrationFactory.class).to(RegistrationFactoryImpl.class);
        bind(HttpRequestAuthorizationService.class).to(JwtHttpRequestAuthorizationService.class);
        bind(IdentityRepository.class).to(IdentityRepositoryImpl.class);
        bind(IdentityFactory.class).to(IdentityFactoryImpl.class);
        bind(PropertiesProvider.class).to(FilePropertiesProvider.class);
        bind(TokenVerificationService.class).annotatedWith(Facebook.class).to(FacebookTokenVerificationService.class);
        bind(TokenVerificationService.class).annotatedWith(Google.class).to(GoogleTokenVerificationService.class);
        bind(TokenVerificationService.class).annotatedWith(Recaptcha.class).to(RecaptchaTokenVerificationService.class);
        bind(MailSenderService.class).to(GenericMailSenderService.class);
    }

    @Provides
    public UserRepositoryImpl userRepositoryImpl() {
        return new UserRepositoryImpl();
    }

    @Provides
    public UserFactoryImpl userFactoryImpl() {
        return new UserFactoryImpl();
    }

    @Provides
    public RegistrationRepositoryImpl registrationRepositoryImpl() {
        return new RegistrationRepositoryImpl();
    }

    @Provides
    public RegistrationFactoryImpl registrationFactoryImpl() {
        return new RegistrationFactoryImpl();
    }

    @Provides
    public JwtHttpRequestAuthorizationService jwtSessionService(UserRepositoryImpl userRepositoryImpl, FilePropertiesProvider filePropertiesProvider) {
        return new JwtHttpRequestAuthorizationService(userRepositoryImpl, filePropertiesProvider);
    }

    @Provides
    public IdentityRepositoryImpl identityRepositoryImpl() {
        return new IdentityRepositoryImpl();
    }

    @Provides
    public IdentityFactoryImpl identityFactoryImpl() {
        return new IdentityFactoryImpl();
    }

    @Provides
    public FilePropertiesProvider filePropertiesProvider() {
        return new FilePropertiesProvider(getClass().getClassLoader().getResourceAsStream("application.properties"));
    }

    @Provides
    public FacebookTokenVerificationService facebookTokenVerificationService(CloseableHttpClient httpClient, FilePropertiesProvider filePropertiesProvider) {
        return new FacebookTokenVerificationService(httpClient, filePropertiesProvider);
    }

    @Provides
    public GoogleTokenVerificationService googleTokenVerificationService(FilePropertiesProvider filePropertiesProvider) {
        return new GoogleTokenVerificationService(filePropertiesProvider);
    }

    @Provides
    public RecaptchaTokenVerificationService recaptchaTokenVerificationService(CloseableHttpClient httpClient, FilePropertiesProvider filePropertiesProvider) {
        return new RecaptchaTokenVerificationService(httpClient, filePropertiesProvider);
    }

    @Provides
    public GenericMailSenderService genericMailSenderService() {
        return new GenericMailSenderService();
    }

    @Provides
    public CloseableHttpClient closeableHttpClient() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
        return httpClient;
    }
}