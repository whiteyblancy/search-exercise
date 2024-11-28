package com.example.searchexercise.dto;

import java.time.LocalDate;
import java.util.List;

public record Company(String company_number,
                      String company_type,
                      String title,
                      String company_status,
                      LocalDate date_of_creation,
                      Address address,
                      List<Officer> officers) {
}
