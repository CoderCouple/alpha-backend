package com.matrix.alpha.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.ServletModule;
import com.matrix.alpha.cors.SimpleCorsFilter;
import com.matrix.alpha.guice.ApiBaseModule;
import com.matrix.alpha.request.ObjectMapperContextResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;

@ApplicationPath("api")
public class Application extends ResourceConfig {
    private static final String API_PACKAGE_PREFIX = ".api";

    @Inject
    public Application(final ServiceLocator serviceLocator) {

        //Register package
        packages(true, "com.matrix.alpha.api");

        // Jackson
        register(ObjectMapperContextResolver.class);
        register(JacksonFeature.class);

        // HK2-Guice Bridge
        Injector injector = createGuiceInjector();
        initGuiceIntoHK2Bridge(serviceLocator, injector);

        // CORSFilter
        register(SimpleCorsFilter.newBuilder()
                .allowOriginDomain("localhost")
                .allowCredentials()
                .allowMethod(HttpMethod.GET)
                .allowMethod(HttpMethod.OPTIONS)
                .allowMethod(HttpMethod.POST)
                .allowMethod(HttpMethod.PUT)
                .allowHeader(HttpHeaders.CONTENT_TYPE)
                .build());

        //Tracing
        property(ServerProperties.TRACING, "ON_DEMAND");
        property(ServerProperties.TRACING_THRESHOLD, "VERBOSE");

        //Jersey BeanValidation
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

    }

    private Injector createGuiceInjector() {
        if (isMac()) {
            return Guice.createInjector(new ServletModule(),new ApiBaseModule());
        } else {
            return Guice.createInjector(Stage.PRODUCTION, new ServletModule(),new ApiBaseModule());
        }
    }

    private void initGuiceIntoHK2Bridge(final ServiceLocator serviceLocator, final Injector injector) {
        GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
        GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
        guiceBridge.bridgeGuiceInjector(injector);
    }

    private boolean isMac() {
        return "mac os x".equalsIgnoreCase(System.getProperty("os.name"));
    }
}
