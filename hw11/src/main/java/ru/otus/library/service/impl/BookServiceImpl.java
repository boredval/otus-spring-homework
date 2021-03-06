package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.dto.BookDto;
import ru.otus.library.dto.CommentDto;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.service.BookSaveException;
import ru.otus.library.service.BookService;

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
    public Flux<BookDto> getAll() {
        return bookRepository.findAll().map(BookDto::fromBook);
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<BookDto> find(String id) {
        return bookRepository.findById(id).map(BookDto::fromBook);
    }

    @Transactional
    @Override
    public Mono<BookDto> save(Book book) {
        try {
            return bookRepository.save(book).map(BookDto::fromBook);
        } catch (Exception e) {
            throw new BookSaveException(String.format("Error on save book: %s, %s", book, e.getMessage()));
        }
    }

    @Transactional
    @Override
    public Mono<Void> delete(Book book) {
        return commentRepository.deleteAll(commentRepository.findById(book.getComment().getId()))
                .zipWith(bookRepository.deleteById(book.getId())).then();
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<CommentDto> getComments(Book book) {
        return commentRepository.findById(book.getComment().getId()).map(CommentDto::fromComment);
    }

    @Transactional
    @Override
    public Mono<CommentDto> addComment(Book book, String text) {
        Comment comment = new Comment();
        comment.setCommentVal(text);
        Comment savedComment = commentRepository.save(comment).block();
        book.setComment(savedComment);
        bookRepository.save(book);
        return commentRepository.findById(savedComment.getId()).map(CommentDto::fromComment);
    }
}
