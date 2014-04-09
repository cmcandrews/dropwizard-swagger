package com.axemblr.dropwizard.swagger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wordnik.swagger.jaxrs.JaxrsApiReader;
import com.wordnik.swagger.jersey.JerseyApiReader;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.servlets.assets.AssetServlet;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * Adds swagger support to a DropWizard project.
 */
public class SwaggerBundle implements ConfiguredBundle<SwaggerUIConfigContainer> {
    /**
     * Extends the AssetServlet to allow the index.html to have the proper
     * baseUrl for the docset.  Otherwise, it was always forced to look at
     * localhost:8080/api-docs.json.  This was always incorrect outside of
     * the development environment.
     */
    private class MustachedIndexAssetServlet extends AssetServlet {
        private final String INDEX_FILE_NAME = "index.html";
        private final Mustache indexFile;
        private final SwaggerUIConfig config;

        public MustachedIndexAssetServlet(SwaggerUIConfig config) {
            super("/swagger-ui/", config.getBaseUrl(), "index.html", Charset.forName("UTF-8"));
            MustacheFactory factory = new DefaultMustacheFactory("swagger-ui/");
            indexFile = factory.compile(INDEX_FILE_NAME);
            this.config = config;
        }

        @Override
        protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setHeader("Allow", "OPTIONS,GET");
            resp.setHeader("Access-Control-Allow-Origin", "*");
            resp.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET");
            resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

            super.doOptions(req, resp);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            if (req.getPathInfo().startsWith("/" + INDEX_FILE_NAME)) {
                StringWriter indexFileWriter = new StringWriter();

                RuntimeSwaggerUIConfig runtimeConfig = new RuntimeSwaggerUIConfig(config, req);
                indexFile.execute(indexFileWriter, runtimeConfig);

                resp.getWriter().print(indexFileWriter.toString());
            } else {
                super.doGet(req, resp);
            }
        }
    }

    @Override
    public void run(SwaggerUIConfigContainer configContainer, Environment environment) throws Exception {
        SwaggerUIConfig config = configContainer.getSwaggerUI();
        if (config.getFormatString() != null) {
            JerseyApiReader.setFormatString(config.getFormatString());
            JaxrsApiReader.setFormatString(config.getFormatString());
        }

        environment.servlets().addServlet("swagger", new MustachedIndexAssetServlet(config))
                .addMapping(config.getBaseUrl() + "*");
        environment.jersey().register(new ApiListingResourceJSON());
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }
}
