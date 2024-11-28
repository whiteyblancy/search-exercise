package com.example.searchexercise;

import com.example.searchexercise.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class SearchService {

    private final SearchClient searchClient;

    public SearchService(SearchClient searchClient) {
        this.searchClient = searchClient;
    }

    public SearchResults<Company> search(SearchCriteria searchCriteria, String apiKey) {
        Predicate<CompanySummary> predicate = searchCriteria.activeOnly() ? CompanySummary::isActive : companySummary -> true;
        List<Company> companyList = searchClient.getCompanySummarySearchResults(searchCriteria.companyCriteria().getSearchTerm(), apiKey)
                .items().stream()
                .filter(predicate)
                .map(companySummary -> mapToCompany(companySummary, apiKey)).toList();
        return new SearchResults<>(companyList.size(), companyList);
    }

    private Company mapToCompany(CompanySummary companySummary, String apiKey) {
        SearchResults<Officer> officerSearchResults = searchClient.getOfficerSearchResults(companySummary.company_number(), apiKey);
        return new Company(companySummary.company_number(), companySummary.company_type(), companySummary.title(), companySummary.company_status(), companySummary.date_of_creation(), companySummary.address(), officerSearchResults.items());
    }
}
