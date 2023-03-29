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
@Table(name = "FUNCTION_ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class FunctionActive {
    @Id
    @Column(name = "CODE", nullable = false, length = 2)
    private String code;

    @Basic
    @Column(name = "TITLE_FUNCTION_ACTIVE", nullable = false, length = 50)
    private String titleFunctionActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODE_TYPE_OBJECT", nullable = false)
    private TypeObject typeObject;

    @Basic
    @Column(name = "ICON", nullable = true, length = 50)
    private String icon;

    @OneToMany(mappedBy="functionActive")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Active> actives;

    public FunctionActive(String code, String titleFunctionActive, TypeObject typeObject, String icon) {
        this.code = code;
        this.titleFunctionActive = titleFunctionActive;
        this.typeObject = typeObject;
        this.icon = icon;
    }

    public FunctionActive() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FunctionActive that = (FunctionActive) o;
        return Objects.equals(code, that.code) && Objects.equals(titleFunctionActive, that.titleFunctionActive) && Objects.equals(typeObject, that.typeObject) && Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, titleFunctionActive, typeObject, icon);
    }

    @Override
    public String toString() {
        return titleFunctionActive;
    }
}
