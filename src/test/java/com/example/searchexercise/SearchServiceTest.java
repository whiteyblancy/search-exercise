package com.example.searchexercise;

import com.example.searchexercise.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchServiceTest {

    private static final String SEARCH_TERM = "searchTerm";
    private static final String API_KEY = "apiKey";

    private static final CompanySummary COMPANY_SUMMARY1_ACTIVE = newCompanySummary(1, "active");
    private static final CompanySummary COMPANY_SUMMARY2_INACTIVE = newCompanySummary(2, "inactive");
    private static final SearchResults<CompanySummary> COMPANY_SUMMARY_RESULTS = new SearchResults<>(2, List.of(COMPANY_SUMMARY1_ACTIVE, COMPANY_SUMMARY2_INACTIVE));

    private static CompanySummary newCompanySummary(int companyTestId, String companyStatus) {
        return new CompanySummary("company_number" + companyTestId, "company_type" + companyTestId, "title" + companyTestId, companyStatus, LocalDate.now().minusWeeks(companyTestId), newAddress(companyTestId));
    }

    private static Address newAddress(int companyTestId) {
        return new Address("locality" + companyTestId, "postal_code" + companyTestId, "premises" + companyTestId, "address_line_1" + companyTestId, "country" + companyTestId);
    }

    private static final SearchResults<Officer> OFFICER_RESULTS_COMPANY1 = new SearchResults<>(2, newOfficerList(1));
    private static final SearchResults<Officer> OFFICER_RESULTS_COMPANY2 = new SearchResults<>(2, newOfficerList(2));

    private static List<Officer> newOfficerList(int companyTestId) {
        List<Officer> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Officer officer = new Officer("name" + companyTestId + i, "officer_role" + companyTestId + i, LocalDate.now().minusWeeks(i), newAddress(companyTestId), null);
            list.add(officer);
        }
        return list;
    }

    private final SearchClient searchClient = mock(SearchClient.class);
    CompanyCriteria companyCriteria = mock(CompanyCriteria.class);


    private final SearchService searchService = new SearchService(searchClient);

    @BeforeEach
    public void setup() {
        when(searchClient.getCompanySummarySearchResults(SEARCH_TERM, API_KEY)).thenReturn(COMPANY_SUMMARY_RESULTS);
        when(searchClient.getOfficerSearchResults("company_number1", API_KEY)).thenReturn(OFFICER_RESULTS_COMPANY1);
        when(searchClient.getOfficerSearchResults("company_number2", API_KEY)).thenReturn(OFFICER_RESULTS_COMPANY2);
        when(companyCriteria.getSearchTerm()).thenReturn(SEARCH_TERM);
    }

    @Test
    public void searchShouldReturnBothActiveAndInactiveCompaniesWhenActiveOnlyFalse() {
        SearchCriteria searchCriteria = new SearchCriteria(companyCriteria, false);



        SearchResults<Company> searchResults = searchService.search(searchCriteria, API_KEY);

        assertThat(searchResults.total_results()).isEqualTo(2);
        Company company1 = searchResults.items().get(0);
        Company company2 = searchResults.items().get(1);

        assertCompany(company1, COMPANY_SUMMARY1_ACTIVE, OFFICER_RESULTS_COMPANY1.items());
        assertCompany(company2, COMPANY_SUMMARY2_INACTIVE, OFFICER_RESULTS_COMPANY2.items());
    }

    @Test
    public void searchShouldReturnOnlyActiveCompaniesWhenActiveOnlyTrue() {
        CompanyCriteria companyCriteria = mock(CompanyCriteria.class);
        SearchCriteria searchCriteria = new SearchCriteria(companyCriteria, true);
        when(companyCriteria.getSearchTerm()).thenReturn(SEARCH_TERM);
        when(searchClient.getCompanySummarySearchResults(SEARCH_TERM, API_KEY)).thenReturn(COMPANY_SUMMARY_RESULTS);
        when(searchClient.getOfficerSearchResults("company_number1", API_KEY)).thenReturn(OFFICER_RESULTS_COMPANY1);
        when(searchClient.getOfficerSearchResults("company_number2", API_KEY)).thenReturn(OFFICER_RESULTS_COMPANY2);

        SearchResults<Company> searchResults = searchService.search(searchCriteria, API_KEY);

        assertThat(searchResults.total_results()).isEqualTo(1);
        Company company1 = searchResults.items().get(0);

        assertCompany(company1, COMPANY_SUMMARY1_ACTIVE, OFFICER_RESULTS_COMPANY1.items());
    }

    private void assertCompany(Company company, CompanySummary expectedSourceOfSummaryFields, List<Officer> expectedOfficers) {
        assertThat(company.company_number()).isEqualTo(expectedSourceOfSummaryFields.company_number());
        assertThat(company.company_type()).isEqualTo(expectedSourceOfSummaryFields.company_type());
        assertThat(company.title()).isEqualTo(expectedSourceOfSummaryFields.title());
        assertThat(company.company_status()).isEqualTo(expectedSourceOfSummaryFields.company_status());
        assertThat(company.date_of_creation()).isEqualTo(expectedSourceOfSummaryFields.date_of_creation());
        assertThat(company.address()).isEqualTo(expectedSourceOfSummaryFields.address());
        assertThat(company.officers()).isEqualTo(expectedOfficers);
    }
}
