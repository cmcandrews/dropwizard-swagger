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
            String newHost = getHostAndPort();
            discoveryUrl = discoveryUrl.replaceAll("\\$HOST", newHost);
        }

        if (discoveryUrl.contains("$BASEURL")) {
            String newBaseUrl = String.format("%s://%s", request.getScheme(), getHostAndPort());
            discoveryUrl = discoveryUrl.replaceAll("\\$BASEURL", newBaseUrl);
        }

        return discoveryUrl;
    }

    private String getHostAndPort() {
        String newHost = request.getServerName();

        if (request.getServerPort() != 80) {
            newHost += ":" + request.getServerPort();
        }
        return newHost;
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
