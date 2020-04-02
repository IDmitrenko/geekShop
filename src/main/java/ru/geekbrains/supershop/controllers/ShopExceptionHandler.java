package ru.geekbrains.supershop.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import ru.geekbrains.supershop.exceptions.ProductNotFoundException;
import ru.geekbrains.supershop.exceptions.ShopInternalServerException;

@Slf4j
@ControllerAdvice
@Api(value = "ShopExceptionHandler", description = "Перехват ошибок Rest-сервиса")
public class ShopExceptionHandler {

    @ApiOperation(value = "Ошибка - продукт не найден.", response = String.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(Model model, final ProductNotFoundException ex) {
        log.error("Product not found thrown", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ApiOperation(value = "Ошибка сервера.", response = String.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ShopInternalServerException.class)
    public String handleShopInternalServerError(Model model, final ShopInternalServerException ex) {
        log.error("Internal Server Error", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}