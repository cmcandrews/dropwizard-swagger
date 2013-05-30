package com.axemblr.dropwizard.swagger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.wordnik.swagger.jaxrs.JaxrsApiReader;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Matt Flower
 */
public class SwaggerUIConfig {
  @JsonProperty
  private String baseUrl = "/swagger-ui/";
  @JsonProperty
  private String discoveryUrl = "/api-docs.json";
  @JsonProperty
  private String apiKey = "special-key";
  @JsonProperty
  private boolean supportHeaderParams = false;
  @JsonProperty
  private List<String> supportedSubmitMethods = Arrays.asList("get", "post", "put", "delete");
  @JsonProperty
  private DocExpansion docExpansion = DocExpansion.none;
  @JsonProperty
  private String formatString = JaxrsApiReader.FORMAT_STRING();

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    if (!baseUrl.endsWith("/")) {
      baseUrl += "/";
    }
    this.baseUrl = baseUrl;
  }

  public String getDiscoveryUrl() {
    return discoveryUrl;
  }

  public void setDiscoveryUrl(String discoveryUrl) {
    this.discoveryUrl = discoveryUrl;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public boolean isSupportHeaderParams() {
    return supportHeaderParams;
  }

  public void setSupportHeaderParams(boolean supportHeaderParams) {
    this.supportHeaderParams = supportHeaderParams;
  }

  public List<String> getSupportedSubmitMethods() {
    return supportedSubmitMethods;
  }

  public String getSupportedSubmitMethodsString() {
    String methodsAsArray = "[]";
    if (supportedSubmitMethods != null && !supportedSubmitMethods.isEmpty()) {
      Joiner j = Joiner.on("', '").skipNulls();
      methodsAsArray = "['" + j.join(supportedSubmitMethods) + "']";
    }

    return methodsAsArray;
  }

  public void setSupportedSubmitMethods(List<String> supportedSubmitMethods) {
    this.supportedSubmitMethods = supportedSubmitMethods;
  }

  public DocExpansion getDocExpansion() {
    return docExpansion;
  }

  public void setDocExpansion(DocExpansion docExpansion) {
    this.docExpansion = docExpansion;
  }

  public String getFormatString() {
    return formatString;
  }

  public void setFormatString(String formatString) {
    this.formatString = formatString;
  }
}
