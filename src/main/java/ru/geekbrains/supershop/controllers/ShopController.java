package ru.geekbrains.supershop.controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.geekbrains.supershop.services.ProductService;

@Controller
@RequiredArgsConstructor
public class ShopController {

    private final ProductService productService;
//это не JSON
    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index(Model model,
                        @RequestParam(required = false) Integer category,
                        @RequestParam(required = false) Boolean available) {

        model.addAttribute("products", productService.findAll(available, category));
        return "index";
    }

}