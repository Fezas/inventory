/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
public class Provider {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;
    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 200)
    private String address;
    @Basic
    @Column(name = "INN", nullable = true, length = 12)
    private String inn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Provider provider = (Provider) o;
        return id == provider.id && Objects.equals(title, provider.title) && Objects.equals(address, provider.address) && Objects.equals(inn, provider.inn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, address, inn);
    }
}
