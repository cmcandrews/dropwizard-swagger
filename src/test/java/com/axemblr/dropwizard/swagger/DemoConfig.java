package com.axemblr.dropwizard.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class DemoConfig extends Configuration implements SwaggerUIConfigContainer {

  @JsonProperty
  private String name;
  private SwaggerUIConfig swaggerui;

  public String getName() {
    return name;
  }

  public SwaggerUIConfig getSwaggerui() {
    return swaggerui;
  }

  @Override
  public SwaggerUIConfig getSwaggerUIConfig() {
    return swaggerui;
  }
}
