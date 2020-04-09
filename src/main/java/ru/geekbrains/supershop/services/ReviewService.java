package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.supershop.exceptions.EntityNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.repositories.ReviewRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    @PersistenceContext
    private EntityManager entityManager;

    private final ReviewRepository reviewRepository;

    public Optional<List<Review>> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    public Optional<List<Review>> getReviewsByShopuser(Shopuser shopuser) {
        return reviewRepository.findByShopuser(shopuser);
    }

    public Optional<Review> getReviewById(UUID id) {
        return reviewRepository.findById(id);
    }


    public void save(Review review) {
        reviewRepository.save(review);
    }

    public UUID moderate(UUID id, String option) throws EntityNotFoundException {
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Oops! Review " + id + " wasn't found!")
        );
        review.setApproved(option.equals("approve"));
        save(review);
        return review.getProduct().getId();
    }

    //Вернуть List комментариев, которых оставил конкретный User
/*
    SELECT
        review.commentary
    FROM
        shopuser
    INNER JOIN
        review ON (shopuser.id = review.shopuser)
    WHERE shopuser.phone = "22222222"
*/
    public List<Review> getReviews(String phone, String email) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
// Обозначаем сущность, которая первостепенно выбирается
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
// Обозначаем корневую сущность на которую будет идти запрос
        Root<Review> root = criteriaQuery.from(Review.class);

        Join<Review, Shopuser> reviewShopuserJoin = root.join("shopuser");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(reviewShopuserJoin.get("phone"), phone));
        if (email != null) {
            predicates.add(criteriaBuilder.equal(reviewShopuserJoin.get("email"), email));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        List<Review> reviews = entityManager.createQuery(criteriaQuery).getResultList();

        return reviews;
    }

    //Вернуть List комментариев, которых оставил конкретный User по конкретному продукту
/*
    SELECT
        review
    FROM
        shopuser
    INNER JOIN
        review ON (shopuser.id = review.shopuser)
    INNER JOIN
        review ON (product.id = review.product)
    WHERE shopuser.phone = "22222222" AND product.title = "Яблоки";
*/
    public List<Review> getReviewsProduct(String phone, String title) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
// Обозначаем сущность, которая первостепенно выбирается
        CriteriaQuery<Review> criteriaQuery = criteriaBuilder.createQuery(Review.class);
// Обозначаем корневую сущность на которую будет идти запрос
        Root<Review> root = criteriaQuery.from(Review.class);

        Join<Review, Shopuser> reviewShopuserJoin = root.join("shopuser");
        Join<Review, Product> reviewProductJoin = root.join("product");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(reviewShopuserJoin.get("phone"), phone));
        if (title != null) {
            predicates.add(criteriaBuilder.equal(reviewProductJoin.get("title"), title));
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

        List<Review> reviews = entityManager.createQuery(criteriaQuery).getResultList();

        return reviews;
    }

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }
}