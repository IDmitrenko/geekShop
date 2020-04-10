package ru.geekbrains.supershop.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.supershop.configurations.SecurityConfig;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductController.class)
@ContextConfiguration(classes = SecurityConfig.class)
public class ProductControllerMVCTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewServiceMock;

    @MockBean
    private ProductService productServiceMock;

    @MockBean
    private ShopuserService shopuserServiceMock;

    @MockBean
    private ImageService imageServiceMock;

    @MockBean
    private AmqpTemplate amqpTemplateMock;

    private Review review;

    @Before
    public void setUp() {

        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        try {
            review = objectMapper
                    .readValue(new ClassPathResource("mocks/review.json").getFile(),
                            new TypeReference<Review>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        given(reviewServiceMock.save(review)).willReturn(review);
    }

    @Test
    public void testMustSaveReview() throws Exception {
        mockMvc
                .perform(post("/products/review")
                .content(objectMapper.writeValueAsString(review))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

}