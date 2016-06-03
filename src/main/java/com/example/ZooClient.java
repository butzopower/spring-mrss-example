package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    public void createMammal(String species, String name, Integer age) {
        String url = zooHost + "/mammals";
        Mammal mammal = new Mammal(species, name, age);
        restTemplate.postForLocation(url, mammal);
    }

    private static class Mammal {
        @JsonProperty
        private final String species;
        @JsonProperty
        private final String name;
        @JsonProperty
        private final Integer age;

        public Mammal(String species, String name, Integer age) {
            this.species = species;
            this.name = name;
            this.age = age;
        }
    }
}
