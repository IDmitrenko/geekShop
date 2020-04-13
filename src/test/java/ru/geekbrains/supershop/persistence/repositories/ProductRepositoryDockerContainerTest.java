package ru.geekbrains.supershop.persistence.repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import org.junit.*;
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

import java.io.IOException;
import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {ProductRepositoryDockerContainerTest.Initializer.class})
public class ProductRepositoryDockerContainerTest {

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

    private List<Product> productMocks;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {

        try {
            productMocks = new ObjectMapper().readValue(new ClassPathResource("mocks/products.json").getFile(), new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        productMocks.forEach(product -> productRepository.save(product));

    }

    @Test
    public void mustFindAllProducts() {
        Assert.assertEquals(4, productRepository.findAll().size());
    }

}