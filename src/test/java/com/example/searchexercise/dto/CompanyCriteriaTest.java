package com.example.searchexercise.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyCriteriaTest {

    private static final String COMPANY_NUMBER = "companyNumber";
    private static final String COMPANY_NAME = "companyName";

    @Test
    public void validShouldBeTrueAndSearchTermShouldBeCompanyNumberWhenBothNumberAndNameAreNonNull() {
        CompanyCriteria criteria = new CompanyCriteria(COMPANY_NUMBER, COMPANY_NAME);

        assertThat(criteria.isValid()).isTrue();
        assertThat(criteria.getSearchTerm()).isEqualTo(COMPANY_NUMBER);
    }

    @Test
    public void validShouldBeTrueAndSearchTermShouldBeCompanyNameWhenNumberIsNull() {
        CompanyCriteria criteria = new CompanyCriteria(null, COMPANY_NAME);

        assertThat(criteria.isValid()).isTrue();
        assertThat(criteria.getSearchTerm()).isEqualTo(COMPANY_NAME);
    }

    @Test
    public void validShouldBeFalseWhenBothCompanyNumberAndNameAreNull() {
        CompanyCriteria criteria = new CompanyCriteria(null, null);

        assertThat(criteria.isValid()).isFalse();
        assertThat(criteria.getSearchTerm()).isNull();
    }
}
