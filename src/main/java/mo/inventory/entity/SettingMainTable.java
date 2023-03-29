/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data
@Entity
@Table(name = "SETTING_MAIN_TABLE", schema = "PUBLIC", catalog = "INVENTORY")
public class SettingMainTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID_USER", nullable = false)
    private long idUser;
    @Basic
    @Column(name = "CLMN_TITLE", nullable = false)
    private boolean clmnTitle;
    @Basic
    @Column(name = "CLMN_MOL", nullable = false)
    private boolean clmnMol;
    @Basic
    @Column(name = "CLMN_NEW_MOL", nullable = false)
    private boolean clmnNewMol;
    @Basic
    @Column(name = "CLMN_AMOUNT", nullable = false)
    private boolean clmnAmount;
    @Basic
    @Column(name = "CLMN_ACCOUNT_NUMBER", nullable = false)
    private boolean clmnAccountNumber;
    @Basic
    @Column(name = "CLMN_FACTORY_NUMBER", nullable = false)
    private boolean clmnFactoryNumber;
    @Basic
    @Column(name = "CLMN_INVENTORY_NUMBER", nullable = false)
    private boolean clmnInventoryNumber;
    @Basic
    @Column(name = "CLMN_DATE_COMISSIONS", nullable = false)
    private boolean clmnDateComissions;
    @Basic
    @Column(name = "CLMN_DATE_ACCOUNTING", nullable = false)
    private boolean clmnDateAccounting;
    @Basic
    @Column(name = "CLMN_FUNCTION_ACTIVE", nullable = false)
    private boolean clmnFunctionActive;
    @Basic
    @Column(name = "CLMN_STATUS_ACTIVE", nullable = false)
    private boolean clmnStatusActive;
    @Basic
    @Column(name = "CLMN_PROVIDER", nullable = true)
    private boolean clmnProvider;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingMainTable that = (SettingMainTable) o;
        return idUser == that.idUser && clmnTitle == that.clmnTitle && clmnMol == that.clmnMol && clmnNewMol == that.clmnNewMol && clmnAmount == that.clmnAmount && clmnAccountNumber == that.clmnAccountNumber && clmnFactoryNumber == that.clmnFactoryNumber && clmnInventoryNumber == that.clmnInventoryNumber && clmnDateComissions == that.clmnDateComissions && clmnDateAccounting == that.clmnDateAccounting && clmnFunctionActive == that.clmnFunctionActive && clmnStatusActive == that.clmnStatusActive && Objects.equals(clmnProvider, that.clmnProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, clmnTitle, clmnMol, clmnNewMol, clmnAmount, clmnAccountNumber, clmnFactoryNumber, clmnInventoryNumber, clmnDateComissions, clmnDateAccounting, clmnFunctionActive, clmnStatusActive, clmnProvider);
    }
}
