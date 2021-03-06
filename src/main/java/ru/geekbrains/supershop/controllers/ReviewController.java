package ru.geekbrains.supershop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.supershop.exceptions.EntityNotFoundException;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.feign.clients.ShopFeignClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Api(value = "ReviewController", description = "Набор методов для отзывов по товарам")
public class ReviewController {

    private final ShopFeignClient shopFeignClient;
    private final ReviewService reviewService;
    private final ImageService imageService;

    @GetMapping("/flyer")
    public ResponseEntity<byte[]> getFlyer() {
        return shopFeignClient.getFlyer();
    }

    @ApiOperation(value = "Модерация отзыва о продукте админом.", response = String.class)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String moderateReview(@PathVariable UUID id, @RequestParam String option) throws EntityNotFoundException {
        return "redirect:/products/" + reviewService.moderate(id, option);
    }

    @ApiOperation(value = "Загрузка картинки с оценкой продукта.", response = String.class)
    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImageReview(@PathVariable String id) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResource(id, true);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAll(), HttpStatus.OK);
    }
}