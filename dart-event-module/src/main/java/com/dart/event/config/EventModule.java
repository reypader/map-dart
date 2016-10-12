package com.dart.event.config;

import com.dart.data.factory.EventFactory;
import com.dart.data.objectify.factory.EventFactoryImpl;
import com.dart.data.objectify.repository.EventRepositoryImpl;
import com.dart.data.objectify.repository.UserRepositoryImpl;
import com.dart.data.repository.EventRepository;
import com.dart.data.repository.UserRepository;
import com.dart.event.service.EventService;
import com.dart.event.service.EventServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.googlecode.objectify.ObjectifyFilter;

import javax.inject.Singleton;

public class EventModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectifyFilter.class).in(Singleton.class);

        bind(EventService.class).to(EventServiceImpl.class);

        //Manually provided
        bind(EventRepository.class).to(EventRepositoryImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(EventFactory.class).to(EventFactoryImpl.class);
    }

    @Provides
    public EventRepositoryImpl eventRepositoryImpl() {
        return new EventRepositoryImpl();
    }

    @Provides
    public UserRepositoryImpl userRepositoryImpl() {
        return new UserRepositoryImpl();
    }

    @Provides
    public EventFactoryImpl eventFactoryImpl() {
        return new EventFactoryImpl();
    }
}