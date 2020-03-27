package ru.geekbrains.supershop.persistence.entities;

import lombok.*;
import ru.geekbrains.supershop.persistence.entities.utils.PersistableEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Review extends PersistableEntity {

    private String commentary;

    @ManyToOne
    @JoinColumn(name = "shopuser")
    private Shopuser shopuser;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    private Boolean approved;

    @OneToOne
    @JoinColumn(name = "image")
    private Image image;
}
