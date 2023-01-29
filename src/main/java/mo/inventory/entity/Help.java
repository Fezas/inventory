/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "HELP", schema = "PUBLIC", catalog = "INVENTORY")
public class Help {
    @Id
    @Column(name = "ID", nullable = false, length = 20)
    private String id;
    @Basic
    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Help help = (Help) o;
        return Objects.equals(id, help.id) && Objects.equals(message, help.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message);
    }
}
