package com.example.searchexercise;

import com.example.searchexercise.dto.Company;
import com.example.searchexercise.dto.CompanyCriteria;
import com.example.searchexercise.dto.SearchCriteria;
import com.example.searchexercise.dto.SearchResults;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public SearchResults<Company> search(@RequestBody @Valid CompanyCriteria companyCriteria, @RequestParam(value = "activeOnly", defaultValue = "false") boolean activeOnly, @RequestHeader(value = "x-api-key") String apiKey) {
        SearchCriteria searchCriteria = new SearchCriteria(companyCriteria, activeOnly);

        return searchService.search(searchCriteria, apiKey);
    }

}

