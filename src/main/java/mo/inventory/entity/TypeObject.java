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
@Table(name = "TYPE_OBJECT", schema = "PUBLIC", catalog = "inventory")
public class TypeObject {
    @Id
    @Column(name = "CODE", nullable = false, length = 2)
    private String code;
    @Basic
    @Column(name = "NAME_TYPE_OBJECT", nullable = false, length = 50)
    private String nameTypeObject;

    @OneToMany(mappedBy="typeObject")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StatusActive> statuses;

    @OneToMany(mappedBy="typeObject")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<FunctionActive> functionActives;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeObject that = (TypeObject) o;
        return Objects.equals(code, that.code) && Objects.equals(nameTypeObject, that.nameTypeObject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, nameTypeObject);
    }

    @Override
    public String toString() {
        return code + " - " + nameTypeObject;
    }
}
