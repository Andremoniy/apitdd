package com.github.andremoniy.apitdd.controller;

import com.github.andremoniy.apitdd.api.controller.BooksApiApi;
import com.github.andremoniy.apitdd.api.model.BookDto;
import com.github.andremoniy.apitdd.api.model.CreateBookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.List;

@Controller
public class BookApiController implements BooksApiApi {
    @Override
    public ResponseEntity<List<BookDto>> apiBooksGet() {
        return ResponseEntity.ok(List.of());
    }

    @Override
    public ResponseEntity<List<BookDto>> apiBooksPost(CreateBookDto createBookDto) {
        return ResponseEntity.created(URI.create("")).build();
    }
}
