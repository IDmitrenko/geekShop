package ru.geekbrains.supershop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.ProductCategory;
import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.services.ReviewService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {ReviewRepositoryDockerContainerTest.Initializer.class})
public class ReviewRepositoryDockerContainerTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(@NotNull ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    private List<Review> reviewsMocks;
    private Product product;
    private Shopuser shopuser;
    private UUID reviewUUID;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopuserRepository shopuserRepository;

    @Before
    public void setUp() {

        product = new Product();
        product.setTitle("Яблоки");
        product.setPrice(150.0);
        product.setAvailable(false);
        product.setCategory(ProductCategory.FOOD);
        product = productRepository.save(product);

        shopuser = new Shopuser();
        shopuser.setPhone("22222222");
        shopuser.setRole(Role.ROLE_CUSTOMER);
        shopuser = shopuserRepository.save(shopuser);

        try {
            reviewsMocks = new ObjectMapper()
                    .readValue(new ClassPathResource("mocks/reviewt.json").getFile(),
                            new TypeReference<List<Review>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

/*
        Optional.ofNullable(reviewsMocks)
                .orElse(Collections.emptyList())
                .forEach(review -> reviewRepository.save(review));
*/
        reviewUUID = null;
        if (reviewsMocks != null) {
            for (Review r : reviewsMocks) {
                r.setShopuser(shopuser);
                r.setProduct(product);
                r = reviewRepository.save(r);
                if (reviewUUID == null) {
                    reviewUUID = r.getId();
                }
            }
        }
    }

    @Test
    public void mustFindReviewsByProduct() {
        Assert.assertEquals(2, reviewRepository.findByProduct(product)
                .orElse(Collections.emptyList()).size());
    }

    @Test
    public void mustFindReviewsByShopuser() {
        Assert.assertEquals(2, reviewRepository.findByShopuser(shopuser)
                .orElse(Collections.emptyList()).size());
    }

    @Test
    public void mustFindReviewsById() {
        Assert.assertEquals(reviewUUID, reviewRepository.findById(reviewUUID).get().getId());
    }
}