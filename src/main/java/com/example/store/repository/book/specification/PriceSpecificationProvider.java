package com.example.store.repository.book.specification;

import com.example.store.model.Book;
import com.example.store.repository.SpecificationProvider;
import java.math.BigDecimal;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PRICE_KEY = "price";

    @Override
    public String getKey() {
        return PRICE_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        BigDecimal[] toBigDecimal = Arrays.stream(params)
            .map(BigDecimal::new)
            .toArray(BigDecimal[]::new);
        return (root, query, criteriaBuilder) -> root.get(PRICE_KEY)
                    .in(toBigDecimal);
    }
}
