/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import javafx.scene.control.CheckBox;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "ABSTRACT_ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class AbstractActive {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;
    @Basic
    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;
    @Basic
    @Column(name = "NOTE", nullable = true, length = 300)
    private String note;
    @Basic
    @Column(name = "PRICE_0", nullable = true, precision = 2)
    private BigDecimal price0;
    @Basic
    @Column(name = "ICON", nullable = true, length = 100)
    private String icon;
    @Basic
    @Column(name = "RESOURCE", nullable = true)
    private Integer resource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CATEGORY_ACTIVE", nullable = false)
    private CategoryActive categoryActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_ACTIVE", nullable = true)
    private TypeActive typeActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OKEI", nullable = true)
    private Okei okei;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TYPE_OBJECT", nullable = true)
    private TypeObject typeObject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROVIDER", nullable = true)
    private Provider provider;

    @OneToMany(mappedBy="abstractActive")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Active> actives;

    @Transient
    private CheckBox remark = new CheckBox();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractActive that = (AbstractActive) o;
        return id == that.id && typeActive == that.typeActive && categoryActive == that.categoryActive && Objects.equals(title, that.title) && Objects.equals(okei, that.okei) && Objects.equals(note, that.note) && Objects.equals(price0, that.price0) && Objects.equals(icon, that.icon) && Objects.equals(resource, that.resource) && Objects.equals(typeObject, that.typeObject) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, okei, note, typeActive, price0, categoryActive, icon, resource, typeObject, provider);
    }
}
