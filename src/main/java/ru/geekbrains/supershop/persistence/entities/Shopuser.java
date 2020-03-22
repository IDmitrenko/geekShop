package ru.geekbrains.supershop.persistence.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.Collection;
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

    @ManyToMany
    @JoinTable(name = "shopuser_role",
            joinColumns = @JoinColumn(name = "shopuser"),
            inverseJoinColumns = @JoinColumn(name = "role"))
    private Collection<Role> roles;

    @OneToMany(mappedBy = "shopuser")
    private List<Purchase> purchases;

}