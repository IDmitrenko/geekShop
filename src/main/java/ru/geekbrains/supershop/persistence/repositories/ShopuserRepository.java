package ru.geekbrains.supershop.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.supershop.persistence.entities.Shopuser;

import java.util.UUID;

@Repository
public interface ShopuserRepository extends CrudRepository<Shopuser, UUID> {
    Shopuser findOneByPhone(String phone);
    boolean existsByPhone(String phone);
}