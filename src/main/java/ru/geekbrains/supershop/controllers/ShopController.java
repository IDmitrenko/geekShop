package ru.geekbrains.supershop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.paymentservice.Payment;
import ru.geekbrains.supershop.beans.Cart;
import ru.geekbrains.supershop.persistence.entities.Product;
import ru.geekbrains.supershop.persistence.entities.Review;
import ru.geekbrains.supershop.persistence.entities.Shopuser;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.ReviewService;
import ru.geekbrains.supershop.services.ShopuserService;
import ru.geekbrains.supershop.services.feign.clients.ShopFeignClient;
import ru.geekbrains.supershop.utils.CaptchaGenerator;
import ru.geekbrains.supershop.utils.Validators;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Api(value = "ShopController", description = "Набор сервисных методов")
public class ShopController {

/* TODO конфиг сервер - отключил (чтобы не запускать еще одну задачу и из-за тестирования)
    @Value("${supershop.city}")
    // Берем properties с конфиг-сервера
    private String cityName;
*/

    private final Cart cart;
    private final CaptchaGenerator captchaGenerator;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final ShopuserService shopuserService;
    private final ShopFeignClient shopFeignClient;
//    private final PriceService priceService;

// Версия отбора через Репозиторий
    @ApiOperation(value = "Главная страница. Список продуктов.", response = String.class)
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model,
                        @RequestParam(required = false) Integer category,
                        @RequestParam(required = false) Integer minPrice,
                        @RequestParam(required = false) Integer maxPrice,
                        @RequestParam(required = false) Boolean notAvailable) {
        model.addAttribute("cart", cart.getCartRecords());
        model.addAttribute("products", productService.findAll(category, minPrice, maxPrice, notAvailable));
        return "index";
    }

/*
    // Версия отбора через entityManager
    @ApiOperation(value = "Главная страница. Список продуктов.", response = String.class)
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model,
                        @RequestParam(required = false) Integer category,
                        @RequestParam(required = false) Boolean available) {
        model.addAttribute("city", cityName);
        model.addAttribute("cart", cart.getCartRecords());
        model.addAttribute("products", productService.getAvailableProductsByCategory(category, available));
        return "index";
    }
*/

    @ApiOperation(value = "Cтраница Администратора.", response = String.class)
    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        model.addAttribute("products", productService.findAll(null, null));
//        model.addAttribute("productsprice", priceService.getProducts(100));

        return "admin";
    }

    @ApiOperation(value = "Страница с данными аутентифицированного пользователя.", response = String.class)
    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        Shopuser shopuser = shopuserService.findByPhone(principal.getName());

        model.addAttribute("reviews", reviewService.getReviewsByShopuser(shopuser).orElse(new ArrayList<>()));
        model.addAttribute("shopuser", shopuser);

        return "profile";
    }

    @ApiOperation(value = "Получить captcha.", response = String.class)
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] captcha(HttpSession session) {
        try {
            BufferedImage img = captchaGenerator.getCaptchaImage();
            session.setAttribute("captchaCode", captchaGenerator.getCaptchaString());
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(img, "png", bao);
            return bao.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "Выбор системы оплаты.", response = String.class)
    @PostMapping("/checkout")
    public String proceedToCheckout(String paymentId, Model model) {

        Payment payment = cart.getPayments()
                .stream()
                .filter(p -> p.getId() == Integer.valueOf(paymentId))
                .collect(Validators.toSingleton());

        cart.setPayment(payment);

        model.addAttribute("cart", cart);

        return "checkout";

    }

    @GetMapping("/superflyer")
    public ResponseEntity<byte[]> getFlyerPDF() throws IOException {
        return shopFeignClient.getFlyer();
    }

    @GetMapping(value = "/rev/{phone}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getReviews(@PathVariable String phone ) {
        return new ResponseEntity<>(reviewService.getReviews(phone, null), HttpStatus.OK);
    }

    @GetMapping(value = "/reviewp/{phone}/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Review>> getReviewsProduct(@PathVariable String phone,
                                                          @PathVariable String title) {
        return new ResponseEntity<>(reviewService.getReviewsProduct(phone, title), HttpStatus.OK);
    }
}