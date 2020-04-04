package ru.geekbrains.supershop.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ru.geekbrains.supershop.persistence.entities.enums.Role;
import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "shopuser")
@EqualsAndHashCode(callSuper = true)
public class Shopuser extends PersistableEntity {

    private String phone;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

/*
    @ManyToMany
    @JoinTable(name = "shopuser_role",
            joinColumns = @JoinColumn(name = "shopuser"),
            inverseJoinColumns = @JoinColumn(name = "role"))
    private Collection<Role> roles;
*/

    @Enumerated(EnumType.STRING)
    private Role role;

/* Обратные ссылки не нужны, так как из-за них получаются циклические зависимости
    @OneToMany(mappedBy = "shopuser")
    private List<Purchase> purchases;

    @OneToMany(mappedBy = "shopuser")
    private List<Review> reviews;
*/

}