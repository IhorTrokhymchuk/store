package com.example.store.dto;

import com.example.store.validation.ArrayNumericValues;
import com.example.store.validation.ArraySizeAndNumericValues;
import lombok.Data;

@Data
public class BookSearchParametersDto {
    private String[] titles;
    private String[] authors;
    private String[] isbns;
    @ArrayNumericValues
    private String[] prices;
    private String[] descriptions;
    private String[] coverImages;
    @ArraySizeAndNumericValues
    private String[] priceMin;
    @ArraySizeAndNumericValues
    private String[] priceMax;
}
