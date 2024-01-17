package com.github.andremoniy.apitdd.specification;

import com.github.andremoniy.apitdd.api.model.BookDetailsDto;
import com.github.andremoniy.apitdd.api.model.BookDto;
import com.github.andremoniy.apitdd.api.model.CreateBookDto;
import com.github.andremoniy.apitdd.api.model.UpdateBookDetailsDto;
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
    @DisplayName("BSP-1 :: Scenario 3: Adding a new book item")
    @Order(300)
    void should_add_new_book_and_retrieve_it() {
        // Given
        CreateBookDto book = new CreateBookDto()
                .title("Designing Data-Intensive Applications")
                .author("Martin Klepperman")
                .isbn("978-1440373320")
                .publisher("O'Reilly Media")
                .publicationDate(LocalDate.of(2017, 3, 16))
                .quantityInStock(5);

        // When
        ResponseEntity<BookDto> addBookResponseEntity = restTemplate.postForEntity("/api/books", book, BookDto.class);

        // Then
        assertTrue(addBookResponseEntity.getStatusCode().is2xxSuccessful());
        BookDto[] books = getBooks();
        assertNotNull(books);
        assertEquals(1, books.length);
        assertEquals(new BookDto()
                .id(1)
                .title("Designing Data-Intensive Applications")
                .author("Martin Klepperman")
                .quantityInStock(5), books[0]);
    }

    private BookDto[] getBooks() {
        ResponseEntity<BookDto[]> bookResponseEntity = restTemplate.getForEntity("/api/books", BookDto[].class);
        assertTrue(bookResponseEntity.getStatusCode().is2xxSuccessful());
        return bookResponseEntity.getBody();
    }

    @Test
    @DisplayName("BSP-1 :: Scenario 4: Deleting a book")
    @Order(400)
    void should_delete_a_book() {
        // Given
        restTemplate.postForEntity("/api/books", new CreateBookDto()
                .title("Specification by Example")
                .author("Gojko Adzic")
                .isbn("978-1617290084")
                .publisher("Manning Publications")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(10), BookDto.class);
        restTemplate.postForEntity("/api/books", new CreateBookDto()
                .title("Sentience: The Invention of Consciousness")
                .author("Nicholas Humphrey")
                .isbn("1111111")
                .publisher("Manning Publications")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(3), BookDto.class);
        restTemplate.postForEntity("/api/books", new CreateBookDto()
                .title("Middlemarch")
                .author("George Eliot")
                .isbn("222222")
                .publisher("Eliot Media")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(2), BookDto.class);
        restTemplate.postForEntity("/api/books", new CreateBookDto()
                .title("The Naked Ape")
                .author("Desmond Morris")
                .isbn("4443444")
                .publisher("Ape Media")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(1), BookDto.class);

        // When
        restTemplate.delete("/api/books/4");

        // Then
        BookDto[] books = getBooks();
        assertEquals(4, books.length);
    }

    @Test
    @DisplayName("BSP-1 :: Scenario 5: Browsing book details")
    @Order(500)
    void should_show_book_item_details() {
        // Given

        // When
        ResponseEntity<BookDetailsDto> bookEntity = restTemplate.getForEntity("/api/books/2", BookDetailsDto.class);

        // Then
        assertTrue(bookEntity.getStatusCode().is2xxSuccessful());

        assertEquals(new BookDetailsDto()
                .id(2)
                .title("Specification by Example")
                .author("Gojko Adzic")
                .isbn("978-1617290084")
                .publisher("Manning Publications")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(10), bookEntity.getBody());
    }

    @Test
    @DisplayName("BSP-1 :: Scenario 6: Updating book details")
    @Order(600)
    void should_update_book_details() {
        // Given
        UpdateBookDetailsDto updateBookDetailsDto = new UpdateBookDetailsDto()
                .quantityInStock(20);

        // When
        restTemplate.patchForObject("/api/books/2", updateBookDetailsDto, Void.class);

        // Then
        ResponseEntity<BookDetailsDto> bookEntity = restTemplate.getForEntity("/api/books/2", BookDetailsDto.class);
        assertTrue(bookEntity.getStatusCode().is2xxSuccessful());

        assertEquals(new BookDetailsDto()
                .id(2)
                .title("Specification by Example")
                .author("Gojko Adzic")
                .isbn("978-1617290084")
                .publisher("Manning Publications")
                .publicationDate(LocalDate.of(2011, 6, 9))
                .quantityInStock(20), bookEntity.getBody());
    }

}
