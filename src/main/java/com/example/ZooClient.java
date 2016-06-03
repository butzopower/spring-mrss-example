package com.example;

import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class ZooClient {
    private final RestTemplate restTemplate;
    private final String zooHost;

    public ZooClient(RestTemplate restTemplate, String zooHost) {
        this.restTemplate = restTemplate;
        this.zooHost = zooHost;
    }

    public List<String> fetchMammals() {
        String url = zooHost + "/mammals";
        String[] mammals = restTemplate.getForObject(url, String[].class);
        return Arrays.asList(mammals);
    }
}
