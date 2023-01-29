/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.dto;

import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import lombok.Data;
import org.kordamp.ikonli.javafx.FontIcon;

@Data
public class CategoryDTO {
    private Long idState;
    private String title;
    private Button btnAddNode;
    private Button btnEdit;
    private Button btnDelete;

    public CategoryDTO(String title, long id) {
        this.title = title;
        this.idState = id;

        btnAddNode = new Button();
        btnAddNode.setGraphic(new FontIcon("anto-plus-circle"));
        Tooltip tooltipAdd = new Tooltip();
        tooltipAdd.setText("Добавить подчиненный узел к \n\"" + title + "\"\n");
        btnAddNode.setTooltip(tooltipAdd);
        btnAddNode.setVisible(false);

        btnEdit = new Button();
        btnEdit.setGraphic(new FontIcon("anto-edit"));
        Tooltip tooltipEdit = new Tooltip();
        tooltipEdit.setText("Корректировка узла \n\"" + title + "\"\n");
        btnEdit.setTooltip(tooltipEdit);
        btnEdit.setVisible(false);

        btnDelete = new Button();
        btnDelete.setGraphic(new FontIcon("anto-delete"));
        Tooltip tooltipDelete = new Tooltip();
        tooltipDelete.setText("Удаление узла \n\"" + title + "\"\n");
        btnDelete.setTooltip(tooltipDelete);
        btnDelete.setVisible(false);
    }

    @Override
    public String toString() {
        return "StateDTO{" +
                "title='" + title + '\'' +
                '}';
    }
}
