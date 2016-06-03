package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class ZooClientTest {
    MockRestServiceServer mockServer;
    ZooClient zooClient;
    String zooHost = "http://thezoo.com";

    @Before
    public void setUp() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        zooClient = new ZooClient(restTemplate, zooHost);
    }

    @Test
    public void testFetchingMammals() throws Exception {
        mockServer
                .expect(requestTo(zooHost + "/mammals"))
                .andRespond(withSuccess("[\"Bear\",\"Jackal\"]", MediaType.APPLICATION_JSON_UTF8));

        List<String> mammals = zooClient.fetchMammals();
        assertThat(mammals, contains("Bear", "Jackal"));
    }

    @Test
    public void testCreatingNewMammal() throws Exception {
        mockServer
                .expect(requestTo(zooHost + "/mammals"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().string("{" +
                        "\"species\":\"Camel\"," +
                        "\"name\":\"Jerry\"," +
                        "\"age\":42" +
                        "}"))
                .andRespond(withSuccess());

        zooClient.createMammal("Camel", "Jerry", 42);

        mockServer.verify();
    }

    @Test
    public void testCreatingNewMammalUsingJSONPath() throws Exception {
        mockServer
                .expect(requestTo(zooHost + "/mammals"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.species", equalTo("Camel")))
                .andExpect(jsonPath("$.name", equalTo("Jerry")))
                .andExpect(jsonPath("$.age", equalTo(42)))
                .andRespond(withSuccess());

        zooClient.createMammal("Camel", "Jerry", 42);

        mockServer.verify();
    }
}
