package ru.geekbrains.supershop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.geekbrains.supershop.beans.Cart;
import ru.geekbrains.supershop.services.ProductService;
import ru.geekbrains.supershop.services.soap.PaymentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
@Api(value = "Набор методов для работы с корзиной заказов")
public class CartController {

    private final Cart cart;
//    private final PaymentService paymentService;
    private final ProductService productService;

    @ApiOperation(value = "Добавить продукт в корзину.", response = String.class)
    @GetMapping("/add/{id}")
    public void addProductToCart(@PathVariable UUID id,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        cart.add(productService.findOneById(id));
        response.sendRedirect(request.getHeader("referer"));
    }

    @ApiOperation(value = "Удалить продукт из корзины.", response = String.class)
    @GetMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable UUID id) {
        cart.removeByProductId(id);
        return "redirect:/cart";
    }

    @ApiOperation(value = "Показать корзину.", response = String.class)
    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cart", cart);
//        model.addAttribute("payments", paymentService.getPayments("Russia"));
        return "cart";
    }

}