package ru.geekbrains.supershop.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.geekbrains.supershop.persistence.entities.Purchase;

import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {}