package com.martinmadai.booklendingapp.domain.bookcopy.service;

import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookCopySpecification {

    public static Specification<BookCopy> titleContains(String title) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.join("book").get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<BookCopy> authorContains(String author) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.join("book").get("author")),
                        "%" + author.toLowerCase() + "%"
                );
    }

    public static Specification<BookCopy> publisherContains(String publisher) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.join("book").get("publisher")),
                        "%" + publisher.toLowerCase() + "%"
                );
    }

    public static Specification<BookCopy> hasCategory(Long categoryId) {
        return (root, query, cb) ->
                cb.equal(
                        root.join("book")
                                .join("categories")
                                .get("id"),
                        categoryId
                );
    }

    public static Specification<BookCopy> hasStatus(BookStatus status) {
        return (root, query, cb) ->
                cb.equal(root.get("status"), status);
    }

    public static Specification<BookCopy> hasCondition(Long conditionId) {
        return (root, query, cb) ->
                cb.equal(root.join("condition").get("id"), conditionId);
    }

    public static Specification<BookCopy> barcodeContains(String barcode) {
        return (root, query, cb) ->
                cb.like(root.get("barcode"), "%" + barcode + "%");
    }
}