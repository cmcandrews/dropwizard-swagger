Dropwizard integration with Wordnik Swagger
-------------------------------------------

To use it you need to:

* add annotations to describe your resources as documented here:
  https://github.com/wordnik/swagger-core/wiki
* put `addBundle(new SwaggerBundle())` in your service constructor 

The JS client should be available at http://localhost:8080/swagger-ui/index.html.

If you need to configure swagger, you can add settings to your config file:

    swaggerUI:
      baseUrl: /swagger-ui/
      discoveryUrl: http://localhost:8080/services/api-docs.json
      supportHeaderParams: true
      supportedSubmitMethods:
        - 'get'
        - 'post'
        - 'put'
        - 'delete'
      docExpansion: list
      formatString:

## Recent Updates
0.1.1
* Updated to support Dropwizard 0.6.2.
* Swagger-ui discovery url is now configurable using constructor `SwaggerBundle(String uriPath, String discoveryUrl)`.
* Ability to configure swagger ui through config files.  

## License
Licensed under the Apache License version 2.0
