package com.example.store.repository.impl;

import com.example.store.model.Book;
import com.example.store.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cant save book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Book bookById = session.find(Book.class, id);
            return Optional.ofNullable(bookById);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book b", Book.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Cant find all from db", e);
        }
    }
}
