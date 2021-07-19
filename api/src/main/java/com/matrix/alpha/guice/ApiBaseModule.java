package com.matrix.alpha.guice;

import com.google.inject.AbstractModule;

public class ApiBaseModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ApiEndpointModule());
    }
}
