package com.example.searchexercise;

import com.example.searchexercise.dto.CompanySummary;
import com.example.searchexercise.dto.Officer;
import com.example.searchexercise.dto.SearchResults;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(SearchClient.class)
public class SearchClientTest {

    private static final String SEARCH_TERM = "searchTerm";
    private static final String COMPANY_NUMBER = "companyNumber";

    private static final String API_KEY = "apiKey";

    private static final CompanySummary COMPANY_SUMMARY1 = newCompanySummary("01234001");
    private static final CompanySummary COMPANY_SUMMARY2 = newCompanySummary("01234002");
    private static final CompanySummary COMPANY_SUMMARY3 = newCompanySummary("01234003");
    private static final List<CompanySummary> COMPANY_SUMMARY_LIST = List.of(COMPANY_SUMMARY1, COMPANY_SUMMARY2, COMPANY_SUMMARY3);
    private static final Officer OFFICER_RESIGNED1 = getOfficerWithNameAndResignedOnDate("John", LocalDate.now());
    private static final Officer OFFICER_RESIGNED2 = getOfficerWithNameAndResignedOnDate("Tom", LocalDate.now());
    private static final Officer OFFICER_ACTIVE1 = getOfficerWithNameAndResignedOnDate("Susan", null);
    private static final Officer OFFICER_ACTIVE2 = getOfficerWithNameAndResignedOnDate("Sharon", null);
    private static final List<Officer> OFFICER_LIST = List.of(OFFICER_RESIGNED1, OFFICER_ACTIVE1, OFFICER_ACTIVE2, OFFICER_RESIGNED2);
    private static final List<Officer> ACTIVE_OFFICER_LIST = List.of(OFFICER_ACTIVE1, OFFICER_ACTIVE2);

    private static CompanySummary newCompanySummary(String companyNumber) {
        return new CompanySummary(companyNumber, "ltd", "comp", "active", null, null);

    }
    private static Officer getOfficerWithNameAndResignedOnDate(String name, LocalDate resignedOnDate) {
        return new Officer(name, "role", LocalDate.now(), null, resignedOnDate);
    }

    private static final String COMPANY_SEARCH_URL = String.format("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query=%s", SEARCH_TERM);

    private static final String OFFICER_SEARCH_URL = String.format("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=%s", COMPANY_NUMBER);

    @Autowired
    MockRestServiceServer server;

    @Autowired
    SearchClient searchClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldGetCompanySummarySearchResultsForSearchTerm() throws JsonProcessingException {
        // given
        SearchResults<CompanySummary> data = new SearchResults<>(3, COMPANY_SUMMARY_LIST);

        // when
        this.server
                .expect(requestTo(COMPANY_SEARCH_URL))
                .andRespond(withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

        // then
        SearchResults<CompanySummary> retrieved = searchClient.getCompanySummarySearchResults(SEARCH_TERM, API_KEY);
        assertThat(retrieved).isEqualTo(data);
    }

    @Test
    public void shouldReturnOnlyActiveOfficers() throws JsonProcessingException {
        // given
        SearchResults<Officer> data = new SearchResults<>(4, OFFICER_LIST);

        // when
        this.server
                .expect(requestTo(OFFICER_SEARCH_URL))
                .andRespond(withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

        // then
        SearchResults<Officer> retrieved = searchClient.getOfficerSearchResults(COMPANY_NUMBER, API_KEY);

        assertThat(retrieved).isEqualTo(new SearchResults<>(2, ACTIVE_OFFICER_LIST));
    }
}
