package ru.otus.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.homework.domain.Author;
import ru.otus.homework.domain.Book;
import ru.otus.homework.domain.Genre;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final AuthorService authorService;
    private final GenreService genreService;
    private final BookRepositoryService bookRepositoryService;



    @Override
    public List<Book> findAll() {
        return bookRepositoryService.findAll();
    }

    @Transactional
    @Override
    public void addBook(Book book) {
        addOrUpdateBook(book);
    }


    @Override
    public Book findById(long id) {
        return bookRepositoryService.findById(id);
    }

    @Override
    @Transactional
    public boolean update(long id, Book book) {
        if (bookRepositoryService.findById(id) != null) {
            addOrUpdateBook(book);
            return true;
        } else return false;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepositoryService.deleteBookById(id);
    }

    private void addOrUpdateBook(Book book) {
        Author author = authorService.findByName(book.getAuthor().getName());
        if (author == null) author = new Author(book.getAuthor().getName());
        Genre genre = genreService.findByName(book.getGenre().getName());
        if (genre == null) genre = new Genre(book.getGenre().getName());
        book.setAuthor(author);
        book.setGenre(genre);
        bookRepositoryService.save(book);
    }


}
