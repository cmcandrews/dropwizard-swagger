package com.axemblr.dropwizard.swagger;

import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.assets.AssetServlet;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Adds swagger support to a DropWizard project.
 */
public class SwaggerBundle implements Bundle {
  /** Default url that the swagger-ui will appear on. */
  public static final String DEFAULT_PATH = "/swagger-ui/";

  private static final Logger LOG = Logger.getLogger(SwaggerBundle.class.getName());
  private static final String DEFAULT_INDEX_FILE = "index.html";
  private String jsonUrl = "/api-docs.json";
  private String uriPath = DEFAULT_PATH;

  /**
   * Extends the AssetServlet to allow the index.html to have the proper
   * baseUrl for the docset.  Otherwise, it was always forced to look at
   * localhost:8080/api-docs.json.  This was always incorrect outside of
   * the development environment.
   *
   * The parameter inside the file looks like a mustache variable, but it
   * isn't -- it wasn't really worth loading mustache for a single
   * variable.
   */
  private class ParameterizedIndexAssetServlet extends AssetServlet {
    private final String INDEX_FILE_NAME = "index.html";
    private String indexFileContents = "";

    public ParameterizedIndexAssetServlet(String resourcePath, String uriPath, String indexFile) {
      super(resourcePath, uriPath, indexFile);
      try {
        InputStream indexFileStream = this.getClass().getClassLoader().getResourceAsStream("swagger-ui/" + INDEX_FILE_NAME);
        BufferedReader reader = new BufferedReader(new InputStreamReader(indexFileStream));

        String line;
        while ((line = reader.readLine()) != null) {
          line = line.replace("{{basePath}}", jsonUrl);
          indexFileContents += line + "\n";
        }
      } catch (IOException e) {
        LOG.log(Level.WARNING, "Unable to read swagger-ui index file", e);
      }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      if (req.getPathInfo().startsWith("/" + INDEX_FILE_NAME)) {
        resp.getWriter().append(indexFileContents);
      } else {
        super.doGet(req, resp);
      }
    }
  }

  /**
   * Initialize a swagger-ui at the url '/swagger-ui' which will look for swagger
   * json file at '/api-docs.json'.
   */
  public SwaggerBundle() {
  }

  /**
   * Initialize a swagger-ui at the url {@code urlPath}
   *
   * @param uriPath String containing the path on which the swagger docs should appear.
   * @param jsonUrl String containing the url to the json to assemble the swagger docs.
   */
  public SwaggerBundle(String uriPath, String jsonUrl) {
    this.uriPath = uriPath.endsWith("/") ? uriPath : (uriPath + '/');
    this.jsonUrl = jsonUrl;
  }


  @Override
  public void initialize(Bootstrap<?> bootstrap) {
  }

  @Override
  public void run(Environment environment) {
    environment.addServlet(new ParameterizedIndexAssetServlet(DEFAULT_PATH, uriPath, DEFAULT_INDEX_FILE), uriPath + "*");
    environment.addResource(new ApiListingResourceJSON());
  }
}
