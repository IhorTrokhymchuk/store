package com.example.store.repository.book;

import com.example.store.dto.book.BookSearchParametersDto;
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

        if (bookSearchParametersDto.getTitles() != null
                && bookSearchParametersDto.getTitles().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("title")
                    .getSpecification(bookSearchParametersDto.getTitles())
            );
        }
        if (bookSearchParametersDto.getAuthors() != null
                && bookSearchParametersDto.getAuthors().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("author")
                    .getSpecification(bookSearchParametersDto.getAuthors())
            );
        }
        if (bookSearchParametersDto.getIsbns() != null
                && bookSearchParametersDto.getIsbns().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("isbn")
                    .getSpecification(bookSearchParametersDto.getIsbns())
            );
        }
        if (bookSearchParametersDto.getPrices() != null
                && bookSearchParametersDto.getPrices().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("price")
                    .getSpecification(bookSearchParametersDto.getPrices())
            );
        }
        if (bookSearchParametersDto.getDescriptions() != null
                && bookSearchParametersDto.getDescriptions().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("description")
                    .getSpecification(bookSearchParametersDto.getDescriptions())
            );
        }
        if (bookSearchParametersDto.getCoverImages() != null
                && bookSearchParametersDto.getCoverImages().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("coverImage")
                    .getSpecification(bookSearchParametersDto.getCoverImages())
            );
        }
        if (bookSearchParametersDto.getPriceMin() != null
                && bookSearchParametersDto.getPriceMin().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("priceMin")
                    .getSpecification(bookSearchParametersDto.getPriceMin())
            );
        }
        if (bookSearchParametersDto.getPriceMax() != null
                && bookSearchParametersDto.getPriceMax().length > 0) {
            specs = specs.and(
                bookSpecificationProviderManager.getSpecificationProvider("priceMax")
                    .getSpecification(bookSearchParametersDto.getPriceMax())
            );
        }

        return specs;
    }
}
