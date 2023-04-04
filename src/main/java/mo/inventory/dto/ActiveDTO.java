/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.dto;

import lombok.Data;
import mo.inventory.entity.*;

import java.sql.Timestamp;
@Data
public class ActiveDTO {
    private long id;
    private AbstractActive abstractActive;
    private Persona persona;
    private Persona newPersona;
    private double amount;
    private String accountNumber;
    private String factoryNumber;
    private String inventoryNumber;
    private Timestamp dateComissioning;
    private Timestamp dateAccounting;
    private FunctionActive functionActive;
    private StatusActive statusActive;
    private Provider provider;
}
