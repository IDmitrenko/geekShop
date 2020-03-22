package ru.geekbrains.supershop.persistence.entities.utils;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.io.Serializable;
import java.util.UUID;

@Data
@MappedSuperclass
public abstract class PersistableEntity implements Serializable {

    private static final long serialVersionUID = -687991492884005033L;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

}
