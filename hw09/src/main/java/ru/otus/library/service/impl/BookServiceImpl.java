package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.service.BookNotFoundException;
import ru.otus.library.service.BookSaveException;
import ru.otus.library.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    public BookServiceImpl(BookRepository bookRepository, CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromBook)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookDto> find(long id) {
        return bookRepository.findById(id).map(BookDto::fromBook);
    }

    @Transactional
    @Override
    public BookDto save(Book book) {
        try {
            Book saved = bookRepository.save(book);
            return BookDto.fromBook(saved);
        } catch (Exception e) {
            throw new BookSaveException(String.format("Error on save book: %s, %s", book, e.getMessage()));
        }
    }

    @Transactional
    @Override
    public void delete(Book book) {
        commentRepository.deleteAll(commentRepository.findByBook(book));
        bookRepository.deleteById(book.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> getComments(Book book) {
        return commentRepository.findByBook(book).stream()
                .map(CommentDto::fromComment).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto addComment(Book book, String text) {
        Book reloaded = bookRepository.findById(book.getId())
                .orElseThrow(BookNotFoundException::new);

        Comment comment = new Comment();
        comment.setValue(text);
        comment.setBook(reloaded);
        commentRepository.save(comment);

        return CommentDto.fromComment(comment);
    }
}
