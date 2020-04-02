package ru.geekbrains.supershop.persistence.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import lombok.NoArgsConstructor;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Product", description = "Entity-класс для единицы продукции.")
public class Product extends PersistableEntity {

    @ApiModelProperty(required = true, value = "Название продукта")
    private String title;

    @ApiModelProperty(required = true, value = "Описание продукта")
    private String description;

    @ApiModelProperty(required = true, value = "Дата добавления продукта")
    private Date added;

    @ApiModelProperty(required = true, value = "Цена продукта")
    private Double price;

    @ApiModelProperty(required = true, value = "Доступность продукта")
    private boolean available;

    @ApiModelProperty(required = true, value = "Категория продукта")
    @Enumerated(EnumType.ORDINAL)
    private ProductCategory category;

    @OneToOne
    @JoinColumn(name = "image")
    @ApiModelProperty(reference = "Image", value = "Фотография продукта")
    private Image image;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

/*
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Image> images;
*/

}