package com.dart.photo.config;

import com.dart.common.service.aop.Authenticated;
import com.dart.common.service.aop.AuthenticationMethodInterceptor;
import com.dart.common.service.auth.HttpRequestAuthorizationService;
import com.dart.common.service.auth.JwtHttpRequestAuthorizationService;
import com.dart.common.service.http.UserAuthorizationFilter;
import com.dart.common.service.properties.FilePropertiesProvider;
import com.dart.common.service.properties.PropertiesProvider;
import com.dart.data.objectify.repository.UserRepositoryImpl;
import com.dart.photo.service.UploadService;
import com.dart.photo.service.google.CloudStorageUploadService;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.matcher.Matchers;
import com.googlecode.objectify.ObjectifyFilter;

import javax.inject.Singleton;

public class PhotoModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectifyFilter.class).in(Singleton.class);
        bind(UserAuthorizationFilter.class).in(Singleton.class);

        bind(UploadService.class).to(CloudStorageUploadService.class);
        bind(PropertiesProvider.class).to(FilePropertiesProvider.class);

        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Authenticated.class),
                new AuthenticationMethodInterceptor());

    }

    @Provides
    @Singleton
    public FilePropertiesProvider filePropertiesProvider() {
        return new FilePropertiesProvider(getClass().getClassLoader().getResourceAsStream("application.properties"));
    }

    @Provides
    @Singleton
    public UserRepositoryImpl userRepositoryImpl() {
        return new UserRepositoryImpl();
    }

    @Provides
    @Singleton
    public HttpRequestAuthorizationService jwtHttpRequestAuthorizationService(UserRepositoryImpl userRepositoryImpl, FilePropertiesProvider filePropertiesProvider) {
        return new JwtHttpRequestAuthorizationService(userRepositoryImpl, filePropertiesProvider);
    }
}