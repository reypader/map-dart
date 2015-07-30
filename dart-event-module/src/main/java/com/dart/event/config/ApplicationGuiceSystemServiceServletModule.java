package com.dart.event.config;

import com.dart.event.api.google.EventEndpoint;
import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.googlecode.objectify.ObjectifyFilter;

import java.util.HashSet;
import java.util.Set;

public class ApplicationGuiceSystemServiceServletModule extends GuiceSystemServiceServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();

        Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
        serviceClasses.add(EventEndpoint.class);
        this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
        this.filter("/*").through(ObjectifyFilter.class);
    }
}
