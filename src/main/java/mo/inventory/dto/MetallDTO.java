/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.dto;

import javafx.beans.property.SimpleStringProperty;

public class MetallDTO {
    private SimpleStringProperty code, nameMetall, shortNameMetall;
    private SimpleStringProperty  weight;

    public MetallDTO(String code, String nameMetall, String shortNameMetall, String weight) {
        this.code = new SimpleStringProperty(code);
        this.nameMetall = new SimpleStringProperty(nameMetall);
        this.shortNameMetall = new SimpleStringProperty(shortNameMetall);
        this.weight = new SimpleStringProperty(weight);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public String getNameMetall() {
        return nameMetall.get();
    }

    public SimpleStringProperty nameMetallProperty() {
        return nameMetall;
    }

    public void setNameMetall(String nameMetall) {
        this.nameMetall.set(nameMetall);
    }

    public String getShortNameMetall() {
        return shortNameMetall.get();
    }

    public SimpleStringProperty shortNameMetallProperty() {
        return shortNameMetall;
    }

    public void setShortNameMetall(String shortNameMetall) {
        this.shortNameMetall.set(shortNameMetall);
    }

    public String getWeight() {
        return weight.get();
    }

    public SimpleStringProperty weightProperty() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight.set(weight);
    }
}
