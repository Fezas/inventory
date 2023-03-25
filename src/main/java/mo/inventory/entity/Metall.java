/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "METALL", schema = "PUBLIC", catalog = "INVENTORY")
public class Metall {
    @Id
    @Column(name = "CODE", nullable = false, length = 3)
    private String code;
    @Basic
    @Column(name = "NAME_METALL", nullable = false, length = 100)
    private String nameMetall;
    @Basic
    @Column(name = "SHORT_NAME_METALL", nullable = false, length = 12)
    private String  shortNameMetall;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Metall metall = (Metall) o;
        return Objects.equals(code, metall.code) && Objects.equals(nameMetall, metall.nameMetall) && Objects.equals(shortNameMetall, metall.shortNameMetall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, nameMetall, shortNameMetall);
    }
}
