package com.example.searchexercise.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import java.util.List;

public record SearchResults<T>(int total_results, @JsonSetter(nulls = Nulls.AS_EMPTY) List<T> items) {
    }

