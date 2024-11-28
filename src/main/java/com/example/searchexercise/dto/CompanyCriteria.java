package com.example.searchexercise.dto;


import jakarta.validation.constraints.AssertTrue;

public record CompanyCriteria(String companyNumber, String companyName) {

    public String getSearchTerm() {
        return companyNumber != null ? companyNumber : companyName;
    }
    //rudimentary validation to demonstrate the concept
    @AssertTrue(message = "At least one of companyNumber or companyName is required")
    public boolean isValid() {
        return getSearchTerm() != null;
    }
}
