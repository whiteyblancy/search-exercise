package com.example.searchexercise.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OfficerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void writeToJsonShouldIncludeResignedOnDateWhenNotNull() throws JsonProcessingException {
        Officer officer = getOfficerWithResignedOnDate(LocalDate.now());

        String json = objectMapper.writeValueAsString(officer);

        assertThat(json).contains("resigned_on");
    }

    @Test
    public void writeToJsonShouldExcludeResignedOnDateWhenNull() throws JsonProcessingException {
        Officer officer = getOfficerWithResignedOnDate(null);

        String json = objectMapper.writeValueAsString(officer);

        assertThat(json).doesNotContain("resigned_on");
    }

    private static Officer getOfficerWithResignedOnDate(LocalDate resignedOnDate) {
        return new Officer(null, null, null, null, resignedOnDate);
    }
}
