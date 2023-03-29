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
@Table(name = "STATUS_ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class StatusActive {
    @Id
    @Column(name = "CODE", nullable = false, length = 2)
    private String code;
    @Basic
    @Column(name = "TITLE_STATUS_ACTIVE", nullable = false, length = 100)
    private String titleStatusActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CODE_TYPE_OBJECT", nullable = false)
    private TypeObject typeObject;

    @OneToMany(mappedBy="statusActive")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Active> actives;

    @Basic
    @Column(name = "ICON", length = 50)
    private String icon;

    public StatusActive() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusActive that = (StatusActive) o;
        return Objects.equals(code, that.code) && Objects.equals(titleStatusActive, that.titleStatusActive) && Objects.equals(typeObject, that.typeObject) && Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, titleStatusActive, typeObject, icon);
    }

    public StatusActive(String code, String nameStatusActive, TypeObject typeObject, String icon) {
        this.code = code;
        this.titleStatusActive = nameStatusActive;
        this.typeObject = typeObject;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return titleStatusActive;
    }
}
