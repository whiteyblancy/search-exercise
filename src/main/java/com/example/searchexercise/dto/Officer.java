package com.example.searchexercise.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public record Officer(String name,
                      String officer_role,
                      LocalDate appointed_on,
                      Address address,
                      @JsonInclude(JsonInclude.Include.NON_NULL) LocalDate resigned_on) {

    @JsonIgnore
    public boolean isActive() {
        return resigned_on == null;
    }
}
