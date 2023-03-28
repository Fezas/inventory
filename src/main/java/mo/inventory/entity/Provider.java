/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "PROVIDER", schema = "PUBLIC", catalog = "INVENTORY")
public class Provider {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;
    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 200)
    private String address;
    @Basic
    @Column(name = "INN", nullable = true, length = 12)
    private String inn;

    @OneToMany(mappedBy="provider")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<AbstractActive> abstractActives;

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

    @Override
    public String toString() {
        return title;
    }
}
