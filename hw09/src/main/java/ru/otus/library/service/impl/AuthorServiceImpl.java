package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.service.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> getById(long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }
}
