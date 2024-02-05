package com.example.store.repository.book;

import com.example.store.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findBookByIsbn(String isbn);

    @Query("SELECT b FROM Book b INNER JOIN FETCH b.categories c WHERE c.id = :id")
    List<Book> findAllByCategoryId(Long id, Pageable pageable);
}
