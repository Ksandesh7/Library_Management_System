package com.tu.libraryManagementSystemBackend.specifications;

import com.tu.libraryManagementSystemBackend.model.Book;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BookSpecifications {
    public static Specification<Book> hasGenre(String genre) {
        return (root, query, criteriaBuilder) ->
                genre==null ? null : criteriaBuilder.equal(root.get("genre"), genre);
    }

    public static Specification<Book> priceLessThanOrEqual(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice==null ? null : criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Book> titleContains(String searchTerm) {
        return (root, query, criteriaBuilder) ->
                searchTerm==null ? null : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%"+searchTerm.toLowerCase()+"%"
                );
    }

    public static Specification<Book> withFilters(
            String genre,
            BigDecimal maxPrice,
            String searchTerm
    ) {
        return Specification.where(hasGenre(genre))
                .and(priceLessThanOrEqual(maxPrice))
                .and(titleContains(searchTerm));
    }

}
