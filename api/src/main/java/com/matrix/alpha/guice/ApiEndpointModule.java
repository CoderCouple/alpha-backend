package com.matrix.alpha.guice;

import com.google.inject.AbstractModule;
import org.reflections.Reflections;

import javax.ws.rs.Path;

public class ApiEndpointModule extends AbstractModule {
    @Override
    protected void configure() {
        Reflections reflections = new Reflections("com.matrix.alpha");
        reflections.getTypesAnnotatedWith(Path.class).forEach(this::bind);
    }
}
