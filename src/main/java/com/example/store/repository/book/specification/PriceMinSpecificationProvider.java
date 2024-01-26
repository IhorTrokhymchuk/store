package com.example.store.repository.book.specification;

import com.example.store.model.Book;
import com.example.store.repository.SpecificationProvider;
import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceMinSpecificationProvider implements SpecificationProvider<Book> {
    private static final String PRICE_KEY = "priceMin";
    private static final BigDecimal MAX_VALUE = BigDecimal.valueOf(Long.MAX_VALUE);

    @Override
    public String getKey() {
        return PRICE_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        BigDecimal minPrice = BigDecimal.valueOf(Double.parseDouble(params[0]));
        return (root, query, criteriaBuilder) -> criteriaBuilder
            .between(root.get("price"), minPrice, MAX_VALUE);
    }
}
