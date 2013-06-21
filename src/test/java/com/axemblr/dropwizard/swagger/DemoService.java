package com.axemblr.dropwizard.swagger;

import com.axemblr.dropwizard.swagger.resources.PetResource;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class DemoService extends Service<DemoConfig> {

    public static void main(String[] args) throws Exception {
        new DemoService().run(args);
    }

    public DemoService() {
    }

    @Override
    public void initialize(Bootstrap<DemoConfig> bootstrap) {
        bootstrap.setName("demo-service");
        bootstrap.addBundle(new SwaggerBundle());
    }

    @Override
    public void run(DemoConfig configuration, Environment environment) throws Exception {
        environment.addResource(new PetResource());
    }
}
