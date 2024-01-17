package com.github.andremoniy.apitdd.controller;

import com.github.andremoniy.apitdd.api.controller.BooksApiApi;
import com.github.andremoniy.apitdd.api.model.BookDetailsDto;
import com.github.andremoniy.apitdd.api.model.BookDto;
import com.github.andremoniy.apitdd.api.model.CreateBookDto;
import com.github.andremoniy.apitdd.api.model.UpdateBookDetailsDto;
import com.github.andremoniy.apitdd.model.Book;
import com.github.andremoniy.apitdd.service.BookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookApiController implements BooksApiApi {

    private final BookService bookService;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<List<BookDto>> apiBooksGet() {
        return ResponseEntity.ok(bookService.findAll()
                .stream()
                .map(book -> modelMapper.map(book, BookDto.class))
                .collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<List<BookDto>> apiBooksPost(CreateBookDto createBookDto) {
        bookService.save(modelMapper.map(createBookDto, Book.class));
        return ResponseEntity.created(URI.create("")).build();
    }

    @Override
    public ResponseEntity<Void> apiBooksIdDelete(Integer id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BookDetailsDto> apiBooksIdGet(Integer id) {
        return ResponseEntity.ok(
                modelMapper.map(bookService.findById(id), BookDetailsDto.class)
        );
    }

    @Override
    public ResponseEntity<BookDetailsDto> apiBooksIdPatch(Integer id, UpdateBookDetailsDto updateBookDetailsDto) {
        Book book = bookService.findById(id);
        book.setQuantityInStock(updateBookDetailsDto.getQuantityInStock());
        return apiBooksIdGet(id);
    }
}
