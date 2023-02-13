/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.inventory.entity.AbstractActive;
import mo.inventory.entity.CategoryActive;
import mo.inventory.entity.Okei;
import mo.inventory.entity.TypeActive;
import mo.inventory.model.AbstractActiveModel;
import mo.inventory.model.CategoryActiveModel;
import mo.inventory.model.OkeiModel;
import mo.inventory.model.TypeActiveModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class AbstractEditController implements Initializable {
    private AbstractActive active;
    private AbstractController controller;

    @FXML    private Button btnCancel;
    @FXML    private Button btnSave;
    @FXML    private Button btnSelectIcon;
    @FXML    private ComboBox<CategoryActive> cmbBoxCategory;
    @FXML    private ComboBox<Okei> cmbBoxOkei;
    @FXML    private ComboBox<TypeActive> cmbBoxType;
    @FXML    private TextField tfTitle, tfPrice0;
    @FXML    private TextArea taNote;

    public AbstractEditController(AbstractActive active) {
        this.active = active;
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        active.setTitle(tfTitle.getText());
        active.setNote(taNote.getText());
        active.setPrice0(new BigDecimal(tfPrice0.getText()));
        active.setCategoryActive(cmbBoxCategory.getSelectionModel().getSelectedItem());
        active.setTypeActive(cmbBoxType.getSelectionModel().getSelectedItem());
        active.setOkei(cmbBoxOkei.getSelectionModel().getSelectedItem());
        AbstractActiveModel.saveOrUpdateCategory(active);
        controller.refresh();
        //controller.init();
        cancel();
    }

    public void setParent (AbstractController controller){
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        cmbBoxCategory.getItems().addAll(CategoryActiveModel.getAll());
        cmbBoxType.getItems().addAll(TypeActiveModel.getAll());
        cmbBoxOkei.getItems().addAll(OkeiModel.getAll());

        if (active.getId() != 0L) { //режим редактирования
            tfTitle.setText(active.getTitle());
            taNote.setText(active.getNote());
            tfPrice0.setText(active.getPrice0().toString());
            cmbBoxCategory.getSelectionModel().select(active.getCategoryActive());
            cmbBoxType.getSelectionModel().select(active.getTypeActive());
            cmbBoxOkei.getSelectionModel().select(active.getOkei());
        }

    }
}
