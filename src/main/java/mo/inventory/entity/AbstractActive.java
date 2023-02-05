/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;
@Data
@Entity
@Table(name = "ABSTRACT_ACTIVE", schema = "PUBLIC", catalog = "INVENTORY")
public class AbstractActive {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 120)
    private String title;
    @Basic
    @Column(name = "ID_OKEI", nullable = true, length = 3)
    private String idOkei;
    @Basic
    @Column(name = "NOTE", nullable = true, length = 200)
    private String note;
    @Basic
    @Column(name = "ID_TYPE_ACTIVE", nullable = false)
    private int idTypeActive;
    @Basic
    @Column(name = "PRICE_0", nullable = true, precision = 2)
    private BigDecimal price0;
    @Basic
    @Column(name = "ID_CATEGORY_ACTIVE", nullable = false)
    private long idCategoryActive;
    @Basic
    @Column(name = "ICON", nullable = true, length = 50)
    private String icon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractActive that = (AbstractActive) o;
        return id == that.id && idTypeActive == that.idTypeActive && idCategoryActive == that.idCategoryActive && Objects.equals(title, that.title) && Objects.equals(idOkei, that.idOkei) && Objects.equals(note, that.note) && Objects.equals(price0, that.price0) && Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, idOkei, note, idTypeActive, price0, idCategoryActive, icon);
    }
}
