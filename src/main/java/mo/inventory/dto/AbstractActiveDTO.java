/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.dto;

import javafx.scene.control.CheckBox;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AbstractActiveDTO {
    private long id;
    private String title;
    private String note;
    private String icon;
    private BigDecimal price0;
    private String categoryActive;
    private String typeActive;
    private String okei;
    private Integer resource;
    private CheckBox remark;

    public AbstractActiveDTO(long id, String title, String note, String icon, BigDecimal price0, String categoryActive, String typeActive, String okei, Integer resource, CheckBox remark) {
        this.id = id;
        this.title = title;
        this.note = note;
        this.icon = icon;
        this.price0 = price0;
        this.categoryActive = categoryActive;
        this.typeActive = typeActive;
        this.okei = okei;
        this.resource = resource;
        this.remark = new CheckBox();
    }

    @Override
    public String toString()  {
        return this.title;
    }
}
