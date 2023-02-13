/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.inventory.entity.FunctionActive;
import mo.inventory.entity.TypeObject;
import mo.inventory.model.FunctionActiveModel;
import mo.inventory.model.TypeObjectActiveModel;
import mo.inventory.util.ValidatorTextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class FunctionEditController implements Initializable {
    private FunctionController controller;
    private FunctionActive current;
    @FXML    private Button btnCancel, btnSave;
    @FXML    private TextField tfCode, tfTitle;
    @FXML    private ComboBox<TypeObject> cmbBoxTypeActive;

    public FunctionEditController(FunctionActive obj) {
        this.current = obj;
    }

    public void setParent (FunctionController controller){
        this.controller = controller;
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        current.setCode(tfCode.getText());
        current.setTitleFunctionActive(tfTitle.getText());
        current.setTypeObject(cmbBoxTypeActive.getSelectionModel().getSelectedItem());
        FunctionActiveModel.saveOrUpdateStructure(current);
        controller.init();
        cancel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbBoxTypeActive.getItems().addAll(TypeObjectActiveModel.getAll());
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        ValidatorTextField validator = new ValidatorTextField();
        validator.validate(tfCode, 2, false, false, true, false);
        validator.validate(tfTitle, 50, true, false, true, true);
        if (current.getCode() != null) { //режим редактирования
            tfCode.setText(current.getCode());
            tfTitle.setText(current.getTitleFunctionActive());
            cmbBoxTypeActive.getSelectionModel().select(current.getTypeObject());
        }
    }
}
