package com.martinmadai.booklendingapp.domain.book.service;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> titleContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")),
                        "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Book> authorContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("author")),
                        "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Book> publisherContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("publisher")),
                        "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Book> hasCategory(Long categoryId) {
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("categories").get("id"), categoryId);
        };
    }
}
