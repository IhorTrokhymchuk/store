package com.example.store.repository.book.specification;

import com.example.store.model.Book;
import com.example.store.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_KEY = "author";

    @Override
    public String getKey() {
        return AUTHOR_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder) -> root.get(AUTHOR_KEY)
            .in(Arrays.stream(params)
                .toArray());
    }
}
