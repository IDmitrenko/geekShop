package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.persistence.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product findOneById(UUID uuid) throws ProductNotFoundException {
        return productRepository.findById(uuid).orElseThrow(
            () -> new ProductNotFoundException("Oops! Product " + uuid + " wasn't found!")
        );
    }

    public List<Product> findAll(Boolean available, Integer category) {
        if (available == null && category == null) {
            return productRepository.findAll();
        }
        if (available == null) {
            return productRepository.findAllByCategory(ProductCategory.values()[category]);
        }
        return category == null ?
                productRepository.findAllByAvailable(available) :
                productRepository.findAllByAvailableAndCategory(available, ProductCategory.values()[category]);

/*
        List<Product> list = new ArrayList<Product>();
        if (category == null) {
            list = productRepository.findAllByAvailable(available);
        } else {
            list = productRepository.findAllByAvailableAndCategory(available, ProductCategory.values()[category]);
        }

        return list;
*/
    }

    @Transactional
    public String save(ProductPojo productPojo, Image image) {

        Product product = Product.builder()
                .added(new Date())
                .title(productPojo.getTitle())
                .description(productPojo.getDescription())
                .price(productPojo.getPrice())
                .available(productPojo.isAvailable())
                .category(productPojo.getCategory())
                .image(image)
                .build();

        productRepository.save(product);
        log.info("New Product has been succesfully added! {}", product);
        return "redirect:/";
    }
}