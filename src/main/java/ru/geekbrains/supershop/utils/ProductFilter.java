package ru.geekbrains.supershop.utils;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.supershop.persistence.entities.Product;

import java.util.Map;

public class ProductFilter {

    private Specification<Product> spec;

    private StringBuilder filterDefinition;

    public ProductFilter(Map<String, String> filterMap) {
        this.spec = Specification.where(null);
        this.filterDefinition = new StringBuilder();

        if (filterMap.containsKey("min_price") && !filterMap.get("min_price").isEmpty()) {
            double minPrice = Double.parseDouble(filterMap.get("min_price"));
            spec = spec.and(ProductSpecification.priceGETthan(minPrice));
            filterDefinition.append("&min_price=").append(minPrice);
        }
    }
}
