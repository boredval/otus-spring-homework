package ru.otus.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.library.dto.BookDto;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookNotFoundException;
import ru.otus.library.service.BookService;
import ru.otus.library.service.GenreService;

@Controller
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public BookController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    public String listPage(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "bookList";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        prepareModel(model, new BookDto());
        return "bookEdit";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDto bookDto = bookService.find(id).orElseThrow(BookNotFoundException::new);
        prepareModel(model, bookDto);
        return "bookEdit";
    }

    @PostMapping("/edit")
    public String saveBook(BookDto bookDto, Model model) {
        BookDto saved = bookService.save(bookDto.toBook());
        prepareModel(model, saved);
        return "bookEdit";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        BookDto bookDto = bookService.find(id).orElseThrow(BookNotFoundException::new);
        bookService.delete(bookDto.toBook());
        return "redirect:/";
    }

    private void prepareModel(Model model, BookDto bookDto) {
        model.addAttribute("book", bookDto);
        model.addAttribute("allAuthors", authorService.getAll());
        model.addAttribute("allGenres", genreService.getAll());
    }

}
