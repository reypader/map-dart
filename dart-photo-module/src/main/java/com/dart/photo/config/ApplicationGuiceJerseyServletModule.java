package com.dart.photo.config;

import com.dart.common.service.http.UserAuthorizationFilter;
import com.google.common.collect.Maps;
import com.googlecode.objectify.ObjectifyFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.Map;

/**
 * @author RMPader
 */
public class ApplicationGuiceJerseyServletModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        Map<String, String> params = Maps.newHashMap();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "io.swagger.jaxrs.json;io.swagger.jaxrs.listing;com.dart.photo.api.jersey");
        params.put(PackagesResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, "com.dart.common.service.http.JerseyCORSFilter");
        params.put(PackagesResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, "com.dart.common.service.http.JerseyCORSFilter");

        serve("/api/*").with(GuiceContainer.class, params);
        this.filter("/api/*").through(ObjectifyFilter.class);
        this.filter("/api/*").through(UserAuthorizationFilter.class);
    }
}
