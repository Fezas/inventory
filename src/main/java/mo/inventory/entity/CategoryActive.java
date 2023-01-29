/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "CATEGORY_ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class CategoryActive {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = -1)
    private String title;
    @Basic
    @Column(name = "ID_CATEGORY", nullable = false)
    private long idCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryActive that = (CategoryActive) o;
        return id == that.id && idCategory == that.idCategory && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, idCategory);
    }
}
