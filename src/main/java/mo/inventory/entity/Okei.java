/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "OKEI", schema = "PUBLIC", catalog = "INVENTORY")
public class Okei {
    @Id
    @Column(name = "CODE", nullable = false, length = 4)
    private String code;
    @Basic
    @Column(name = "NAME_UNIT", nullable = false, length = 80)
    private String nameUnit;
    @Basic
    @Column(name = "SYMBOL", nullable = false, length = 8)
    private String symbol;
    @Basic
    @Column(name = "ID_TYPE_UNIT", nullable = false)
    private int idTypeUnit;
    @Basic
    @Column(name = "FLAG_ACTUAL", nullable = false)
    private boolean flagActual;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Okei okei = (Okei) o;
        return idTypeUnit == okei.idTypeUnit && flagActual == okei.flagActual && Objects.equals(code, okei.code) && Objects.equals(nameUnit, okei.nameUnit) && Objects.equals(symbol, okei.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, nameUnit, symbol, idTypeUnit, flagActual);
    }
}
