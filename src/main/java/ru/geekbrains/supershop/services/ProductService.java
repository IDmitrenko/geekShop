package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.supershop.exceptions.EntityNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.persistence.repositories.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProductRepository productRepository;

    public Product findOneById(UUID uuid) throws EntityNotFoundException {
        return productRepository.findById(uuid).orElseThrow(
                () -> new EntityNotFoundException("Oops! Product " + uuid + " wasn't found!")
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

    public List<Product> findAll(Integer category, Integer minPrice, Integer maxPrice, Boolean notAvailable) {

        if (category == null && minPrice == null &&
                maxPrice == null && notAvailable == null) {
            return productRepository.findAll();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();

        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), category));
        }

        if (minPrice != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (notAvailable != null) {
            predicates.add(criteriaBuilder.isTrue(root.get("available")));
        }

        criteriaQuery.select(root);  // если в запросе много сущностей (указываем из какой выбираем)
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

/*
    public List<Product> getAvailableProductsByCategory(Integer category, Boolean available) {
        if (available == null && category == null) {
            return productRepository.findAll();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
// Обозначаем сущность, которая первостепенно выбирается
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
// Обозначаем корневую сущность на которую будет идти запрос
        Root<Product> root = criteriaQuery.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(criteriaBuilder.equal(root.get("category"), ProductCategory.values()[category]));
        }
        if (available != null) {
            predicates.add(criteriaBuilder.equal(root.get("available"), available));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        List<Product> productList = entityManager.createQuery(criteriaQuery).getResultList();

        return productList;
    }
*/

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

/*
    @EventListener
    public void myListener(SuperShopEvent event) {

    }
*/
}