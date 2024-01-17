package com.github.andremoniy.apitdd.service;

import com.github.andremoniy.apitdd.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BookService {

    private final List<Book> books = new ArrayList<>();
    private final AtomicInteger idSequence = new AtomicInteger();

    public Collection<Book> findAll() {
        return books;
    }

    public void save(Book book) {
        book.setId(idSequence.incrementAndGet());
        books.add(book);
    }

    public void deleteById(Integer id) {
        books.removeIf(book -> id.equals(book.getId()));
    }

    public Book findById(Integer id) {
        return books.stream().filter(book -> id.equals(book.getId())).findAny().orElse(null);
    }
}
