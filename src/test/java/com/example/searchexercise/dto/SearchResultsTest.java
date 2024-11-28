package com.example.searchexercise.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SearchResultsTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void readFromJsonShouldConvertMissingItemsIntoEmptyList() throws JsonProcessingException {
        String json = "{}";

        SearchResults<Officer> searchResults = objectMapper.readValue(json, SearchResults.class);

        assertThat(searchResults.items()).isEmpty();

    }

    @Test
    public void readFromJsonShouldConvertNullItemsIntoEmptyList() throws JsonProcessingException {
        String json = "{\"items\":null}";

        SearchResults<Officer> searchResults = objectMapper.readValue(json, SearchResults.class);

        assertThat(searchResults.items()).isEmpty();

    }
}
