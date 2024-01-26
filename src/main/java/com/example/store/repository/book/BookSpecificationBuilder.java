package com.example.store.repository.book;

import com.example.store.dto.BookSearchParametersDto;
import com.example.store.model.Book;
import com.example.store.repository.SpecificationBuilder;
import com.example.store.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specs = Specification.where(null);

        if (bookSearchParametersDto.titles() != null
                && bookSearchParametersDto.titles().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.titles())
            );
        }
        if (bookSearchParametersDto.authors() != null
                && bookSearchParametersDto.authors().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.authors())
            );
        }
        if (bookSearchParametersDto.isbns() != null
                && bookSearchParametersDto.isbns().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParametersDto.isbns())
            );
        }
        if (bookSearchParametersDto.prices() != null
                && bookSearchParametersDto.prices().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("price")
                    .getSpecification(bookSearchParametersDto.prices())
            );
        }
        if (bookSearchParametersDto.descriptions() != null
                && bookSearchParametersDto.descriptions().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("description")
                    .getSpecification(bookSearchParametersDto.descriptions())
            );
        }
        if (bookSearchParametersDto.coverImages() != null
                && bookSearchParametersDto.coverImages().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("coverImage")
                    .getSpecification(bookSearchParametersDto.coverImages())
            );
        }
        if (bookSearchParametersDto.priceMin() != null
                && bookSearchParametersDto.priceMin().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("priceMin")
                    .getSpecification(bookSearchParametersDto.priceMin())
            );
        }
        if (bookSearchParametersDto.priceMax() != null
                && bookSearchParametersDto.priceMax().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("priceMax")
                    .getSpecification(bookSearchParametersDto.priceMax())
            );
        }

        return specs;
    }
}
