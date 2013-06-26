Dropwizard integration with Wordnik Swagger
-------------------------------------------

To use it you need to:

* add annotations to describe your resources as documented here:
  https://github.com/wordnik/swagger-core/wiki
* put `addBundle(new SwaggerBundle())` in your service constructor 

The JS client should be available at http://localhost:8080/swagger-ui/index.html.

To configure swagger, you can add settings to your config file:

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

This will require that your configuration file implement SwaggerUIConfigContainer.

## Maven Repo Info

        <dependency>
            <groupId>com.axemblr.dropwizard</groupId>
            <artifactId>dropwizard-swagger</artifactId>
            <version>0.2.1-SNAPSHOT</version>
        </dependency>

        <repository>
            <id>dropwizard-swagger-mvn-repo</id>
            <url>https://raw.github.com/MattFlower/dropwizard-swagger/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>


## Recent Updates
0.2.3
* Add $BASEURL environment variable to the discoveryUrl.  This is similar to $HOST, but also includes the scheme.
For example discoveryUrl: $BASEURL/services/api-docs.json

0.2.2
* You can now use $HOST in the discoveryUrl, e.g. `discoveryUrl: http://$HOST/services/api-docs.json`.  This should
allow a little bit more flexibility in environments where there are many hostnames.  $HOST encompasses both the server
name and the port if the port is non-80.

0.2.1
* Fixed problem with 0.2.0 - the swagger-ui version and swagger-jersey-jaxrs_2.10.0 versions didn't match exactly which was causing methods to not be found.

0.2.0
* Added configuration settings which allows swagger to be configurable.

0.1.1
* Updated to support Dropwizard 0.6.2.
* Swagger-ui discovery url is now configurable using constructor `SwaggerBundle(String uriPath, String discoveryUrl)`.
* Ability to configure swagger ui through config files.  

## License
Licensed under the Apache License version 2.0
