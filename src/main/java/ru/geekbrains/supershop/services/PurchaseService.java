package ru.geekbrains.supershop.services;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.geekbrains.supershop.persistence.entities.Purchase;
import ru.geekbrains.supershop.persistence.repositories.PurchaseRepository;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    @Transactional
    public Purchase makePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

}