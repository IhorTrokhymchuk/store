package com.example.store.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns,
        String[] prices,
        String[] descriptions,
        String[] coverImages,
        String[] priceMin,
        String[] priceMax) {
}
