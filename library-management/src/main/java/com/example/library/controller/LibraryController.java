package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.service.LibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // Home / Books list
    @GetMapping({"/", "/books"})
    public String listBooks(Model model) {
        model.addAttribute("books", libraryService.getAllBooks());
        return "books";
    }

    // Add book form
    @GetMapping("/books/new")
    public String showAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    // Save book
    @PostMapping("/books")
    public String saveBook(@ModelAttribute Book book) {
        libraryService.saveBook(book);
        return "redirect:/books";
    }

    // Edit book
    @GetMapping("/books/edit/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = libraryService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
        model.addAttribute("book", book);
        return "add-book";
    }

    // Delete book
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return "redirect:/books";
    }

    // Members list
    @GetMapping("/members")
    public String listMembers(Model model) {
        model.addAttribute("members", libraryService.getAllMembers());
        return "members";
    }

    // Borrow form
    @GetMapping("/borrow/{id}")
    public String showBorrowForm(@PathVariable Long id, Model model) {
        model.addAttribute("bookId", id);
        model.addAttribute("members", libraryService.getAllMembers());
        return "borrow";
    }

    // Process borrow
    @PostMapping("/borrow")
    public String borrowBook(@RequestParam Long bookId, Model model) {
        if (libraryService.borrowBook(bookId)) {
            return "redirect:/books";
        } else {
            model.addAttribute("error", "Cannot borrow - no copies available");
            model.addAttribute("books", libraryService.getAllBooks());
            return "books";
        }
    }

    // Return book
    @GetMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        libraryService.returnBook(id);
        return "redirect:/books";
    }
}