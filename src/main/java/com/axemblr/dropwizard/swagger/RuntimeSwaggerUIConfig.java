package com.axemblr.dropwizard.swagger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Matt Flower
 */
public class RuntimeSwaggerUIConfig {
    private final SwaggerUIConfig swaggerUIConfig;
    private final HttpServletRequest request;

    public RuntimeSwaggerUIConfig(SwaggerUIConfig swaggerUIConfig, HttpServletRequest request) {
        this.swaggerUIConfig = swaggerUIConfig;
        this.request = request;
    }

    public String getBaseUrl() {
        return swaggerUIConfig.getBaseUrl();
    }

    public String getDiscoveryUrl() {
        String discoveryUrl = swaggerUIConfig.getDiscoveryUrl();

        if (discoveryUrl.contains("$HOST")) {
            String newHost = request.getServerName();

            if (request.getServerPort() != 80) {
                newHost += ":" + request.getServerPort();
            }

            discoveryUrl = discoveryUrl.replaceAll("\\$HOST", newHost);
        }

        return discoveryUrl;
    }

    public String getApiKey() {
        return swaggerUIConfig.getApiKey();
    }

    public boolean isSupportHeaderParams() {
        return swaggerUIConfig.isSupportHeaderParams();
    }

    public List<String> getSupportedSubmitMethods() {
        return swaggerUIConfig.getSupportedSubmitMethods();
    }

    public String getSupportedSubmitMethodsString() {
        return swaggerUIConfig.getSupportedSubmitMethodsString();
    }

    public DocExpansion getDocExpansion() {
        return swaggerUIConfig.getDocExpansion();
    }

    public String getFormatString() {
        return swaggerUIConfig.getFormatString();
    }
}
