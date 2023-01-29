/*
 * Copyright (c) 2022. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mo.inventory.entity.Structure;
import mo.inventory.model.StructureModel;
import mo.inventory.util.InnChecker;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class StructureEditController implements Initializable {
    private Structure structure;
    private long id;
    private StructureController controller;

    public StructureEditController(Structure structure, long id) {
        this.structure = structure;
        this.id = id;
    }

    @FXML    private Button btnCancel;
    @FXML    private Button btnSave;
    @FXML    private TextArea taAddress;
    @FXML    private TextField tfInn;
    @FXML    private TextField tfTitle;
    @FXML    private Label lblINN;

    public TreeTableView<Structure> tblStructureSecond;

    public void setParent (StructureController controller){
        this.controller = controller;
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        StructureModel model = new StructureModel();
        structure.setTitle(tfTitle.getText());
        structure.setInn(tfInn.getText());
        structure.setAddress(taAddress.getText());
        if (structure.getId() == 0L) { //создание нового узла
            structure.setIdStructure(id);
        }
        model.saveOrUpdateStructure(structure);
        controller.refresh();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        ValidationSupport validationSupport = new ValidationSupport();
        validationSupport.registerValidator(tfTitle, Validator.createEmptyValidator("Text is required"));
        tfInn.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue != null) {
                    if (!newValue.matches("\\d*")) {
                        tfInn.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (tfInn.getText().length() > 10) {
                        String s = tfInn.getText().substring(0, 10);
                        tfInn.setText(s);
                    }
                    if (!InnChecker.checkInn(newValue)) {
                        lblINN.setStyle("-fx-text-fill: red;");
                        lblINN.setText("ИНН не корректен!");
                    } else {
                        lblINN.setStyle("-fx-text-fill: green;");
                        lblINN.setText("ИНН корректен");
                    }
                }
            }
        });

        if (structure.getId() != 0L) { //режим редактирования
            tfTitle.setText(structure.getTitle());
            tfInn.setText(structure.getInn());
            taAddress.setText(structure.getAddress());
        }
    }
}
