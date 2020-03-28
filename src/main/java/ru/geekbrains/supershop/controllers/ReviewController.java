package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.geekbrains.supershop.exceptions.EntityNotFoundException;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ReviewService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ImageService imageService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String moderateReview(@PathVariable UUID id, @RequestParam String option) throws EntityNotFoundException {
        return "redirect:/products/" + reviewService.moderate(id, option);
    }

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
}