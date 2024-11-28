package com.example.searchexercise.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public record CompanySummary(String company_number,
                             String company_type,
                             String title,
                             String company_status,
                             LocalDate date_of_creation,
                             Address address) {

    @JsonIgnore
    public boolean isActive() {
        return "active".equals(company_status);
    }
}