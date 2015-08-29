package com.dart.photo.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class ApplicationGuiceServletContextListener extends GuiceServletContextListener {

//        @Override
//    protected Injector getInjector() {
//        return Guice.createInjector(new ApplicationGuiceSystemServiceServletModule(), new PhotoModule());
//    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new ApplicationGuiceJerseyServletModule(), new PhotoModule());
    }

}
