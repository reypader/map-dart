package com.dart.user.config;

import com.dart.common.service.http.CORSFilter;
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
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, "io.swagger.jaxrs.json;io.swagger.jaxrs.listing;com.dart.user.api.jersey");

        serve("/api/*").with(GuiceContainer.class, params);
        this.filter("/*").through(ObjectifyFilter.class);
        this.filter("/*").through(CORSFilter.class);
        this.filter("/api/*").through(UserAuthorizationFilter.class);
    }
}
