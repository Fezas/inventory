/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "PERSONA", schema = "PUBLIC", catalog = "INVENTORY")
public class Persona {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "POSITION", nullable = true, length = 100)
    private String position;
    @Basic
    @Column(name = "FAMILY", nullable = false, length = 20)
    private String family;
    @Basic
    @Column(name = "NAME", nullable = false, length = 15)
    private String name;
    @Basic
    @Column(name = "LASTNAME", nullable = false, length = 20)
    private String lastname;
    @Basic
    @Column(name = "STRUCTURE_ID", nullable = false)
    private long structureId;
    @Basic
    @Column(name = "PASSWORD", nullable = true, length = 8)
    private String password;
    @Basic
    @Column(name = "ID_ROLE", nullable = false, columnDefinition = "1")
    private int idRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return id == persona.id && structureId == persona.structureId && idRole == persona.idRole && Objects.equals(position, persona.position) && Objects.equals(family, persona.family) && Objects.equals(name, persona.name) && Objects.equals(lastname, persona.lastname) && Objects.equals(password, persona.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, family, name, lastname, structureId, password, idRole);
    }
}
