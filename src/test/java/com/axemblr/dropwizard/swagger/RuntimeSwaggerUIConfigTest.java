package com.axemblr.dropwizard.swagger;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Matt Flower
 */
public class RuntimeSwaggerUIConfigTest {
    private HttpServletRequest request;
    private RuntimeSwaggerUIConfig runtimeConfig;
    private SwaggerUIConfig swaggerConfig;

    @Before
    public void setUp() {
        request = Mockito.mock(HttpServletRequest.class);

        swaggerConfig = new SwaggerUIConfig();
        runtimeConfig = new RuntimeSwaggerUIConfig(swaggerConfig, request);
    }

    @Test
    public void testGetDiscoveryUrlWithBaseUrl() throws Exception {
        when(request.getScheme()).thenReturn("https");
        when(request.getServerName()).thenReturn("abc.com");
        when(request.getServerPort()).thenReturn(1234);
        swaggerConfig.setDiscoveryUrl("$BASEURL/a/b/c");

        assertThat(runtimeConfig.getDiscoveryUrl(), equalTo("https://abc.com:1234/a/b/c"));
    }

    @Test
    public void testGetDiscoveryUrlWithHost() throws Exception {
        when(request.getServerName()).thenReturn("def.com");
        when(request.getServerPort()).thenReturn(5678);
        swaggerConfig.setDiscoveryUrl("http://$HOST/d/e/f");

        assertThat(runtimeConfig.getDiscoveryUrl(), equalTo("http://def.com:5678/d/e/f"));
    }
}
