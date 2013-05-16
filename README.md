Dropwizard integration with Wordnik Swagger
-------------------------------------------

To use it you need to:

* add annotations to describe your resources as documented here:
  https://github.com/wordnik/swagger-core/wiki
* put `addBundle(new SwaggerBundle())` in your service constructor 

The JS client should be available at http://localhost:8080/swagger-ui/index.html.

## Recent Updates
0.1.1
* Updated to support Dropwizard 0.6.2.
* Swagger-ui discovery url is now configurable using constructor `SwaggerBundle(String uriPath, String discoveryUrl)`.
* Swagger-core now updated to

## License
Licensed under the Apache License version 2.0
