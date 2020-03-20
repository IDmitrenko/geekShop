package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        List<Product> list = new ArrayList<Product>();
        if (category == null) {
            list = productRepository.findAllByAvailable(available);
        } else {
            list = productRepository.findAllByAvailableAndCategory(available, ProductCategory.values()[category]);
        }

        return list;
/*
        return category == null ?
                productRepository.findAllByAvailable(available) :
                productRepository.findAllByAvailableAndCategory(available, ProductCategory.values()[category]);
*/
    }

}