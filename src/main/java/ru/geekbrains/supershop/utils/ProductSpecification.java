package ru.geekbrains.supershop.utils;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.supershop.persistence.entities.Product;

public class ProductSpecification {

    public static Specification<Product> priceGETthan(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
    }
}
