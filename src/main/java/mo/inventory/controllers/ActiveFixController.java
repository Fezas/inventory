/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import mo.inventory.entity.Persona;

import java.net.URL;
import java.util.ResourceBundle;

public class ActiveFixController implements Initializable {
    private MainController controller;
    private Persona persona;
    @FXML    private Button btnCancel, btnSave;

    @FXML    private ComboBox<?> cmbFunctionActive;
    @FXML    private ComboBox<?> cmbProvider;
    @FXML    private ComboBox<?> cmbStatusActive;

    @FXML    private DatePicker dateAccounting;
    @FXML    private DatePicker dateCommissioning;

    @FXML    private Label lblNameMol;

    @FXML    private TextField tfAccountNumber, tfAmount, tfFactoryNumber, tfInventoryNumber;

    public void setParent (MainController controller){
        this.controller = controller;
    }

    public ActiveFixController(Persona persona) {
        this.persona = persona;
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        cancel();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblNameMol.setText(persona.getFamily() + " " + persona.getName().charAt(0) + "." + persona.getLastname().charAt(0) + ".");
    }

}
