package ru.geekbrains.supershop.persistence.pojo;

import lombok.Data;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;

@Data
public class ProductPojo {
    private String title;
    private String description;
    private Double price;
    private boolean available;
    private ProductCategory category;
}