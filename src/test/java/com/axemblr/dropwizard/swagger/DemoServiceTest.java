package com.axemblr.dropwizard.swagger;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class DemoServiceTest {

  private static File configFile;
  private static int port;

  @BeforeClass
  public static void setUpClass() throws IOException, InterruptedException {
    //Find a valid port to run the service on
    ServerSocket openPort = new ServerSocket(0);
    port = openPort.getLocalPort();
    openPort.close();

    configFile = File.createTempFile("dropwizard-swagger", ".yml");
    configFile.deleteOnExit();

    Files.write((
            "name: Test\n" +
            "http:\n" +
            "    port: " + port + "\n" +
            "swaggerUI:\n" +
            "    baseUrl: /swagger-ui2/\n" +
            "    discoveryUrl: /api-docs2.json\n" +
            "    apiKey: special-key2\n" +
            "    supportHeaderParams: true\n" +
            "    supportedSubmitMethods: \n" +
            "        - 'get'\n" +
            "        - 'post'\n" +
            "        - 'put'\n" +
            "        - 'delete'\n" +
            "    docExpansion: list\n" +
            "    formatString: '.test123'\n").getBytes(Charsets.UTF_8), configFile);

    Thread runner = new Thread() {
      @Override
      public void run() {
        try {
          DemoService.main(new String[]{"server", configFile.getAbsolutePath()});
        } catch (Exception e) {
          throw Throwables.propagate(e);
        }
      }
    };

    runner.setDaemon(true);
    runner.start();

    // TODO replace with wait for port
    TimeUnit.SECONDS.sleep(5);
  }

  private void assertUriExists(String uri) throws IOException {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet("http://localhost:" + port + uri);

    HttpResponse response = client.execute(get);

    if (uri.contains("index.html")) {
      response.getEntity().writeTo(System.out);
    }
    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPetResource() throws IOException {
    assertUriExists("/pet.json");
  }

  @Test
  public void testSwaggerUI() throws IOException {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet("http://localhost:" + port + "/swagger-ui2/index.html");
    HttpResponse response = client.execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    response.getEntity().writeTo(out);

    String webpage = out.toString();

    //Make sure that our configuration happened
    assertThat(webpage, Matchers.containsString("/api-docs2.json"));
    assertThat(webpage, Matchers.containsString("special-key2"));
    assertThat(webpage, Matchers.containsString("supportHeaderParams: true"));
    assertThat(webpage, Matchers.containsString("supportedSubmitMethods: ['get', 'post', 'put', 'delete']"));
    assertThat(webpage, Matchers.containsString("docExpansion: \"list\""));
  }

  @Test
  public void testFormatString() throws IOException {
    HttpClient client = new DefaultHttpClient();
    HttpGet get = new HttpGet("http://localhost:" + port + "/api-docs.json");
    HttpResponse response = client.execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    response.getEntity().writeTo(out);

    String webpage = out.toString();
    System.out.println(webpage);

    assertThat(webpage, Matchers.containsString("/api-docs.test123/pet"));
  }

  @Test
  public void testApiDescription() throws IOException {
    assertUriExists("/api-docs.json");
    assertUriExists("/api-docs.json/pet");
  }
}
