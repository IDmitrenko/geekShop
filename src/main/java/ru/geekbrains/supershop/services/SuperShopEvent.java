package ru.geekbrains.supershop.services;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class SuperShopEvent extends ApplicationEvent {

    @Getter
    private UUID uuid;

    public SuperShopEvent(Object source, UUID uuid) {
        super(source);
        this.uuid =uuid;
    }
}
