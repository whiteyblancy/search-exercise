package com.example.searchexercise;

import com.example.searchexercise.dto.CompanySummary;
import com.example.searchexercise.dto.Officer;
import com.example.searchexercise.dto.SearchResults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class SearchClient {

    private final RestClient restClient;

    public SearchClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public SearchResults<CompanySummary> getCompanySummarySearchResults(String searchTerm, String apiKey) {
        return restClient.get()
                .uri("/Search?Query={search_term}", searchTerm)
                .header("x-api-key", apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public SearchResults<Officer> getOfficerSearchResults(String companyNumber, String apiKey) {
        SearchResults<Officer> searchResults = restClient.get()
                .uri("/Officers?CompanyNumber={companyNumber}", companyNumber)
                .header("x-api-key", apiKey)
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        List<Officer> activeOfficerList = searchResults.items().stream().filter(Officer::isActive).toList();

        return new SearchResults<>(activeOfficerList.size(), activeOfficerList);
    }
}
