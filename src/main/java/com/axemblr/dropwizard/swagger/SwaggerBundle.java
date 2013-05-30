package com.axemblr.dropwizard.swagger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.wordnik.swagger.jaxrs.JaxrsApiReader;
import com.wordnik.swagger.jersey.JerseyApiReader;
import com.wordnik.swagger.jersey.listing.ApiListingResourceJSON;
import com.yammer.dropwizard.ConfiguredBundle;
import com.yammer.dropwizard.assets.AssetServlet;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

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
    private final String indexFileContents;


    public MustachedIndexAssetServlet(SwaggerUIConfig config) {
      super("/swagger-ui/", config.getBaseUrl(), "index.html");
      MustacheFactory factory = new DefaultMustacheFactory("swagger-ui/");
      Mustache indexFile = factory.compile(INDEX_FILE_NAME);
      StringWriter indexFileWriter = new StringWriter();
      indexFile.execute(indexFileWriter, config);
      indexFileContents = indexFileWriter.toString();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      if (req.getPathInfo().startsWith("/" + INDEX_FILE_NAME)) {
        resp.getWriter().print(indexFileContents);
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

    environment.addServlet(new MustachedIndexAssetServlet(config), config.getBaseUrl() + "*");
    environment.addResource(new ApiListingResourceJSON());
  }

  @Override
  public void initialize(Bootstrap<?> bootstrap) {
  }
}
