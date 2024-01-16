package com.github.andremoniy.apitdd.specification;

import com.github.andremoniy.apitdd.api.model.BookDto;
import com.github.andremoniy.apitdd.api.model.CreateBookDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BooksSpecification {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DisplayName("BSP-1 :: Scenario 1: Browsing an empty books catalogue")
    @Order(100)
    void should_retrieve_an_empty_books_catalogue() {
        // Given

        // When
        ResponseEntity<BookDto[]> bookResponseEntity = restTemplate.getForEntity("/api/books", BookDto[].class);

        // Then
        assertTrue(bookResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(bookResponseEntity.getBody());
        assertEquals(0, bookResponseEntity.getBody().length);
    }

    @Test
    @DisplayName("BSP-2 :: Scenario 3: Adding a new book item")
    @Order(300)
    void should_add_new_book_and_retrieve_it() {
        // Given
        CreateBookDto book = new CreateBookDto()
                .title("Designing Data-Intensive Applications")
                .author("Martin Klepperman")
                .isbn("978=1440373320")
                .publisher("O'Reilly Media")
                .publicationDate(LocalDate.of(2017, 3, 16))
                .quantityInStock(5);

        // When
        ResponseEntity<BookDto>  addBookResponseEntity = restTemplate.postForEntity("/api/books", book, BookDto.class);

        // Then
        assertTrue(addBookResponseEntity.getStatusCode().is2xxSuccessful());
        ResponseEntity<BookDto[]> bookResponseEntity = restTemplate.getForEntity("/api/books", BookDto[].class);
        assertTrue(bookResponseEntity.getStatusCode().is2xxSuccessful());
        assertNotNull(bookResponseEntity.getBody());
        assertEquals(1, bookResponseEntity.getBody().length);
    }

}
