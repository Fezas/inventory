/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "METALL_IN_ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class MetallInActive {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "CODE_METALL", nullable = false, length = 3)
    private String codeMetall;
    @Basic
    @Column(name = "ID_ABSTRACT_ACTIVE", nullable = false)
    private long idAbstractActive;
    @Basic
    @Column(name = "WEIGHT", nullable = false, precision = 0)
    private double weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetallInActive that = (MetallInActive) o;
        return idAbstractActive == that.idAbstractActive && Double.compare(that.weight, weight) == 0 && id == that.id && Objects.equals(codeMetall, that.codeMetall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codeMetall, idAbstractActive, weight, id);
    }
}
