package com.axemblr.dropwizard.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class DemoConfig extends Configuration implements SwaggerUIConfigContainer {

    @JsonProperty
    private String name;
    private SwaggerUIConfig swaggerUI;

    public String getName() {
        return name;
    }

    @Override
    public SwaggerUIConfig getSwaggerUI() {
        return swaggerUI;
    }

}
