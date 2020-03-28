package ru.geekbrains.supershop.controllers;

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
public class CartController {

    private final Cart cart;
    private final PaymentService paymentService;
    private final ProductService productService;

    @GetMapping("/add/{id}")
    public void addProductToCart(@PathVariable UUID id,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        cart.add(productService.findOneById(id));
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable UUID id) {
        cart.removeByProductId(id);
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("payments", paymentService.getPayments("Russia"));
        return "cart";
    }

}