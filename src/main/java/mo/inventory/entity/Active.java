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
    @Basic
    @Column(name = "ID_ABSTRACT_ACTIVE", nullable = false)
    private long idAbstractActive;
    @Basic
    @Column(name = "ID_PERSONA", nullable = true)
    private Long idPersona;
    @Basic
    @Column(name = "AMOUNT", nullable = false, precision = 0)
    private double amount;
    @Basic
    @Column(name = "ACCOUNT_NUMBER", nullable = true, length = -1)
    private String accountNumber;
    @Basic
    @Column(name = "FACTORY_NUMBER", nullable = true, length = -1)
    private String factoryNumber;
    @Basic
    @Column(name = "INVENTORY_NUMBER", nullable = true, length = -1)
    private String inventoryNumber;
    @Basic
    @Column(name = "DATE_COMISSIONING", nullable = true)
    private Timestamp dateComissioning;
    @Basic
    @Column(name = "DATE_ACCOUNTING", nullable = true)
    private Timestamp dateAccounting;
    @Basic
    @Column(name = "ID_FUNCTION_ACTIVE", nullable = true, length = -1)
    private String idFunctionActive;
    @Basic
    @Column(name = "ID_STATUS_ACTIVE", nullable = true, length = -1)
    private String idStatusActive;
    @Basic
    @Column(name = "ID_PROVIDER", nullable = true)
    private Long idProvider;
    @Basic
    @Column(name = "RESERV_1", nullable = true)
    private Long reserv1;
    @Basic
    @Column(name = "RESERV_2", nullable = true)
    private Long reserv2;
    @Basic
    @Column(name = "RESERV_3", nullable = true, length = -1)
    private String reserv3;
    @Basic
    @Column(name = "RESERV_4", nullable = true)
    private Long reserv4;
    @Basic
    @Column(name = "RESERV_5", nullable = true, length = -1)
    private String reserv5;
    @Basic
    @Column(name = "DATE_RECORD_CREATION", nullable = false)
    private Timestamp dateRecordCreation;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Active active = (Active) o;
        return id == active.id && idAbstractActive == active.idAbstractActive && Double.compare(active.amount, amount) == 0 && Objects.equals(idPersona, active.idPersona) && Objects.equals(accountNumber, active.accountNumber) && Objects.equals(factoryNumber, active.factoryNumber) && Objects.equals(inventoryNumber, active.inventoryNumber) && Objects.equals(dateComissioning, active.dateComissioning) && Objects.equals(dateAccounting, active.dateAccounting) && Objects.equals(idFunctionActive, active.idFunctionActive) && Objects.equals(idStatusActive, active.idStatusActive) && Objects.equals(idProvider, active.idProvider) && Objects.equals(reserv1, active.reserv1) && Objects.equals(reserv2, active.reserv2) && Objects.equals(reserv3, active.reserv3) && Objects.equals(reserv4, active.reserv4) && Objects.equals(reserv5, active.reserv5) && Objects.equals(dateRecordCreation, active.dateRecordCreation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, idAbstractActive, idPersona, amount, accountNumber, factoryNumber, inventoryNumber, dateComissioning, dateAccounting, idFunctionActive, idStatusActive, idProvider, reserv1, reserv2, reserv3, reserv4, reserv5, dateRecordCreation);
    }
}
