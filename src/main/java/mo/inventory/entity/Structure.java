/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(name = "STRUCTURE", schema = "PUBLIC", catalog = "INVENTORY")
public class Structure {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;
    @Basic
    @Column(name = "ID_STRUCTURE", nullable = false)
    private long idStructure;
    @Basic
    @Column(name = "INN", nullable = true, length = 10)
    private String inn;
    @Basic
    @Column(name = "ADDRESS", nullable = true, length = 150)
    private String address;
    @Basic
    @Column(name = "IS_ROOT", nullable = false, columnDefinition = "false")
    private boolean isRoot;

    public Structure(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Structure structure = (Structure) o;
        return id == structure.id && idStructure == structure.idStructure && isRoot == structure.isRoot && Objects.equals(title, structure.title) && Objects.equals(inn, structure.inn) && Objects.equals(address, structure.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, idStructure, inn, address, isRoot);
    }
}
