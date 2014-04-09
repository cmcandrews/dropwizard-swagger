package com.axemblr.dropwizard.swagger;

import com.axemblr.dropwizard.swagger.resources.PetResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DemoService extends Application<DemoConfig> {

    public static void main(String[] args) throws Exception {
        new DemoService().run(args);
    }

    public DemoService() {
    }

    @Override
    public void initialize(Bootstrap<DemoConfig> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle());
    }

    @Override
    public void run(DemoConfig configuration, Environment environment) throws Exception {
        environment.jersey().register(new PetResource());
    }
}
