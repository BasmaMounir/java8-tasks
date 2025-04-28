package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class StreamsExample {

    public static void main(final String[] args) {

        List<Author> authors = Library.getAuthors();

        banner("Authors information");
        // SOLVED With functional interfaces declared
        Consumer<Author> authorPrintConsumer = new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                System.out.println(author);
            }
        };
        authors
                .stream()
                .forEach(authorPrintConsumer);

        // SOLVED With functional interfaces used directly
        authors
                .stream()
                .forEach(System.out::println);

        banner("Active authors");
        // TODO With functional interfaces declared
        Predicate<Author> isActive = new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.active;
            }
        };

        authors
                .stream().filter(isActive).forEach(author -> System.out.println(author.name));

        banner("Active authors - lambda");
        // TODO With functional interfaces used directly
        authors.stream()
                .filter(author -> author.active)
                .forEach(author -> System.out.println(author.name));

        banner("Active books for all authors");
        // TODO With functional interfaces declared
        Consumer<Author> activeBook = new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                author.books.stream()
                        .filter(book -> book.published)
                        .forEach(book -> System.out.println(book.name));
            }
        };
        authors
                .stream()
                .forEach(activeBook);

        banner("Active books for all authors - lambda");
        // TODO With functional interfaces used directly
        authors.stream()
                .flatMap(author -> author.books.stream().filter(book -> book.published))
                .forEach(book -> System.out.println(book.name));


        banner("Average price for all books in the library");
        // TODO With functional interfaces declared
        Function<List<Author>,Double> averagePrice =new Function<List<Author>,Double>() {
            @Override
            public Double apply(List<Author> auths) {
                return auths.stream()
                        .flatMap(author -> author.books.stream())
                        .mapToInt(book -> book.price).average().orElse(0.0);
            }
        };
        System.out.println(averagePrice.apply(authors));


        banner("Average price for all books in the library - lambda");
        // TODO With functional interfaces used directly
        System.out.println(authors.stream().
                flatMap(author -> author.books.stream())
                .mapToInt(book -> book.price).
                average().orElse(0.0));

        banner("Active authors that have at least one published book");
        // TODO With functional interfaces declared
        Consumer<Author> activeAuthorsWithBooks = new Consumer<Author>() {
            @Override
            public void accept(Author author) {
                boolean isPublished = author
                        .books.stream()
                        .anyMatch(book -> book.published);
                if (isPublished && author.active) {
                    System.out.println(author.name);
                }
            }
        };
        authors
                .stream()
                .forEach(activeAuthorsWithBooks);

        banner("Active authors that have at least one published book - lambda");
        // TODO With functional interfaces used directly
        authors.stream()
                .filter(author -> author.active)
                .filter(author ->author.books.stream().anyMatch(book -> book.published))
                .forEach(auth -> System.out.println(auth.name));
    }

    private static void banner(final String m) {
        System.out.println("#### " + m + " ####");
    }
}


class Library {
    public static List<Author> getAuthors() {
        return Arrays.asList(
            new Author("Author A", true, Arrays.asList(
                new Book("A1", 100, true),
                new Book("A2", 200, true),
                new Book("A3", 220, true))),
            new Author("Author B", true, Arrays.asList(
                new Book("B1", 80, true),
                new Book("B2", 80, false),
                new Book("B3", 190, true),
                new Book("B4", 210, true))),
            new Author("Author C", true, Arrays.asList(
                new Book("C1", 110, true),
                new Book("C2", 120, false),
                new Book("C3", 130, true))),
            new Author("Author D", false, Arrays.asList(
                new Book("D1", 200, true),
                new Book("D2", 300, false))),
            new Author("Author X", true, Collections.emptyList()));
    }
}

class Author {
    String name;
    boolean active;
    List<Book> books;

    Author(String name, boolean active, List<Book> books) {
        this.name = name;
        this.active = active;
        this.books = books;
    }

    @Override
    public String toString() {
        return name + "\t| " + (active ? "Active" : "Inactive");
    }
}

class Book {
    String name;
    int price;
    boolean published;

    Book(String name, int price, boolean published) {
        this.name = name;
        this.price = price;
        this.published = published;
    }

    @Override
    public String toString() {
        return name + "\t| " + "\t| $" + price + "\t| " + (published ? "Published" : "Unpublished");
    }
}
