package ru.geekbrains.supershop.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.geekbrains.supershop.persistence.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findOneByName(String name);
}