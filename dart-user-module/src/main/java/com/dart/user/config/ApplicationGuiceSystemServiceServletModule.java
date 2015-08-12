package com.dart.user.config;

import com.dart.user.api.google.UserEndpoint;
import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.googlecode.objectify.ObjectifyFilter;

import java.util.HashSet;
import java.util.Set;

public class ApplicationGuiceSystemServiceServletModule extends GuiceSystemServiceServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();

        Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
        serviceClasses.add(UserEndpoint.class);
        this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
        this.filter("/*").through(ObjectifyFilter.class);
    }
}
