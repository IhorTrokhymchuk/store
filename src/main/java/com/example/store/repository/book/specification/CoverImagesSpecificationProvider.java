package com.example.store.repository.book.specification;

import com.example.store.model.Book;
import com.example.store.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CoverImagesSpecificationProvider implements SpecificationProvider<Book> {
    private static final String COVER_IMAGE_KEY = "coverImage";

    @Override
    public String getKey() {
        return COVER_IMAGE_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {

        return (root, query, criteriaBuilder) -> root.get(COVER_IMAGE_KEY)
            .in(Arrays.stream(params)
                .toArray());
    }
}
