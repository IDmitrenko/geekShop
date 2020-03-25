package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.exceptions.UnsupportedMediaTypeException;
import ru.geekbrains.supershop.exceptions.WrongCaptchaCodeException;
import ru.geekbrains.supershop.persistence.entities.Image;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.persistence.pojo.ProductPojo;
import ru.geekbrains.supershop.persistence.pojo.ReviewPojo;
import ru.geekbrains.supershop.services.ImageService;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;
import ru.geekbrains.supershop.services.utils.CHelper;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ImageService imageService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ShopuserService shopuserService;

    @GetMapping("/{id}")
    public String getOneProduct(Model model, @PathVariable String id) throws ProductNotFoundException {

//        if (CHelper.parseUUIDString(id)) {
        Product product = productService.findOneById(UUID.fromString(id));
        List<Review> reviews = reviewService.getReviewsByProduct(product).orElse(new ArrayList<>());
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "product";
//        }
//        return "redirect:/";
    }

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable String id) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResource(id);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    @GetMapping(value = "/reviews/images/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImageReview(@PathVariable String id) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResourceImage(id);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }

    @GetMapping("/reviews/{id}")
    public String approvedReview(@PathVariable(value = "id") UUID id) {
        Review review = reviewService.getReviewById(id).orElse(null);
        if (review != null) {
            review.setApproved(true);
            reviewService.save(review);
        }
        return "redirect:/products/" + review.getProduct().getId();
    }

    @PostMapping
    public String addProduct(@RequestParam("image") MultipartFile image,
                         ProductPojo productPojo)
            throws IOException, UnsupportedMediaTypeException {
        Image img = imageService.uploadImage(image, productPojo.getTitle());
        return productService.save(productPojo, img);
    }

    @PostMapping("/reviews")
    public String addReview(@RequestParam("image") MultipartFile image,
                            ReviewPojo reviewPojo, HttpSession session, Principal principal)
            throws ProductNotFoundException, WrongCaptchaCodeException,
                   IOException, UnsupportedMediaTypeException {

        if (reviewPojo.getCaptchaCode().equals(session.getAttribute("captchaCode"))) {

            Product product = productService.findOneById(reviewPojo.getProductId());
            Shopuser shopuser = shopuserService.findByPhone(principal.getName());
            Boolean approved = shopuser.getRole().equals(Role.ROLE_ADMIN);
            Image img = imageService.uploadImage(image, principal.getName() + "_" + reviewPojo.getProductId());

            Review review = Review.builder()
                    .commentary(reviewPojo.getCommentary())
                    .product(product)
                    .shopuser(shopuser)
                    .approved(approved)
                    .image(img)
                    .build();

            reviewService.save(review);

            return "redirect:/products/" + product.getId();

        } else {
            throw new WrongCaptchaCodeException("Error! Captcha code is incorrect! Please try again!");
        }

    }
/*
    @GetMapping(value = "/imagesn/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getImages(@PathVariable String id) throws IOException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedImage bufferedImage = imageService.loadFileAsResourceImage(id);
        if (bufferedImage != null) {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } else {
            return new byte[0];
        }
    }
*/

}