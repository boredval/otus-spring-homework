package ru.otus.library.repository;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.library.model.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
