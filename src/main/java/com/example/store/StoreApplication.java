package com.example.store;

import com.example.store.model.Book;
import com.example.store.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StoreApplication {
    private final BookService bookService;

    @Autowired
    public StoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(StoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book newBook = new Book();
            newBook.setTitle("book_title");
            newBook.setAuthor("Trokhymchuk Ihor");
            newBook.setPrice(BigDecimal.valueOf(12.99));
            newBook.setIsbn("ISBN_NEW_BOOK");
            bookService.save(newBook);
            System.out.println(bookService.findAll());
        };
    }
}
