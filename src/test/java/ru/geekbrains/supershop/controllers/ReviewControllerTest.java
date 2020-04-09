package ru.geekbrains.supershop.controllers;

import org.junit.Before;
import org.junit.Test;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.feign.clients.ShopFeignClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReviewControllerTest {

    private ReviewController reviewController;

    private List<Review> reviews = new ArrayList<Review>() {{
        add(new Review());
        add(new Review());
        add(new Review());
        add(new Review());
    }};

    @Before
    public void setUp() {

        ShopFeignClient shopFeignClientMock = mock(ShopFeignClient.class);
        ReviewService reviewServiceMock = mock(ReviewService.class);
        ImageService imageServiceMock = mock(ImageService.class);

        reviewController = new ReviewController(shopFeignClientMock, reviewServiceMock, imageServiceMock);

        when(reviewServiceMock.getAll()).thenReturn(reviews);

    }

    @Test
    public void getReviewsTest() throws Exception {
        List<Review> testReviews = reviewController.getAllReviews().getBody();
        assertEquals(4, testReviews.size());
    }

}