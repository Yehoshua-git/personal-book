package com.example.demo.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleBookService {
    private final RestClient restClient;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.books.base-url}")
    private String baseUrl;

    public Map getBookById(String googleId) {

        String url = baseUrl + "/volumes/" + googleId;

        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(Map.class);
    }

    public GoogleBookService(@Value("${google.books.base-url:https://www.googleapis.com/books/v1}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public GoogleBook searchBooks(String query, Integer maxResults, Integer startIndex) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/volumes")
                        .queryParam("q", query)
                        .queryParam("maxResults", maxResults != null ? maxResults : 10)
                        .queryParam("startIndex", startIndex != null ? startIndex : 0)
                        .build())
                .retrieve()
                .body(GoogleBook.class);
    }
}

