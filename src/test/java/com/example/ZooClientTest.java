package com.example;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
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
}
