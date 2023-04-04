/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Objects;

@Data
@Entity
@Table(name = "ACTIVE", schema = "PUBLIC", catalog = "inventory")
public class Active {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ABSTRACT_ACTIVE", nullable = true)
    private AbstractActive abstractActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONA", nullable = true)
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_NEW_PERSONA", nullable = true)
    private Persona newPersona;

    @Basic
    @Column(name = "AMOUNT", nullable = false, precision = 0)
    private double amount;
    @Basic
    @Column(name = "ACCOUNT_NUMBER", nullable = true, length = 50)
    private String accountNumber;
    @Basic
    @Column(name = "FACTORY_NUMBER", nullable = true, length = 50)
    private String factoryNumber;
    @Basic
    @Column(name = "INVENTORY_NUMBER", nullable = true, length = 50)
    private String inventoryNumber;
    @Basic
    @Column(name = "DATE_COMISSIONING", nullable = true)
    private Timestamp dateComissioning;
    @Basic
    @Column(name = "DATE_ACCOUNTING", nullable = true)
    private Timestamp dateAccounting;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FUNCTION_ACTIVE", nullable = true)
    private FunctionActive functionActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_STATUS_ACTIVE", nullable = true)
    private StatusActive statusActive;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PROVIDER", nullable = true)
    private Provider provider;

    @Basic
    @Column(name = "RESERV_1", nullable = true)
    private Long reserv1;
    @Basic
    @Column(name = "RESERV_2", nullable = true)
    private Long reserv2;
    @Basic
    @Column(name = "RESERV_3", nullable = true, length = 200)
    private String reserv3;
    @Basic
    @Column(name = "RESERV_4", nullable = true)
    private Long reserv4;
    @Basic
    @Column(name = "RESERV_5", nullable = true, length = 200)
    private String reserv5;
    @Basic
    @Column(name = "DATE_RECORD_CREATION", nullable = false)
    private Timestamp dateRecordCreation;

    @Transient
    private String categoryAbstractActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Active active = (Active) o;
        return id == active.id && abstractActive == active.abstractActive && Double.compare(active.amount, amount) == 0 &&
                Objects.equals(persona, active.persona) && Objects.equals(newPersona, active.newPersona) && Objects.equals(accountNumber, active.accountNumber) &&
                Objects.equals(factoryNumber, active.factoryNumber) && Objects.equals(inventoryNumber, active.inventoryNumber) &&
                Objects.equals(dateComissioning, active.dateComissioning) && Objects.equals(dateAccounting, active.dateAccounting) &&
                Objects.equals(functionActive, active.functionActive) && Objects.equals(statusActive, active.statusActive) &&
                Objects.equals(provider, active.provider) && Objects.equals(reserv1, active.reserv1) &&
                Objects.equals(reserv2, active.reserv2) && Objects.equals(reserv3, active.reserv3) && Objects.equals(reserv4, active.reserv4) &&
                Objects.equals(reserv5, active.reserv5) && Objects.equals(dateRecordCreation, active.dateRecordCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, abstractActive, persona, newPersona, amount, accountNumber, factoryNumber, inventoryNumber,
                dateComissioning, dateAccounting, functionActive, statusActive, provider, reserv1, reserv2, reserv3,
                reserv4, reserv5, dateRecordCreation);
    }

}
