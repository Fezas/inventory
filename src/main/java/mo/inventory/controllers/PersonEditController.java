/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mo.inventory.entity.Persona;
import mo.inventory.model.PersonaModel;
import mo.inventory.util.ValidatorTextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class PersonEditController implements Initializable {

    private Persona persona;
    private long id;
    private StructureController controller;

    public PersonEditController(Persona persona, long id) {
        this.persona = persona;
        this.id = id;
    }
    @FXML    private Button btnCancel, btnSave;
    @FXML    private TextField tfFamily, tfLastname, tfName, tfPosition, tfPassword;

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
        PersonaModel model = new PersonaModel();
        persona.setFamily(tfFamily.getText());
        persona.setName(tfName.getText());
        persona.setLastname(tfLastname.getText());
        persona.setPosition(tfPosition.getText());
        persona.setPassword(tfPassword.getText());
        persona.setIdRole(1);
        if (persona.getId() == 0L) { //создание нового узла
            persona.setStructureId(id);
        }
        model.saveOrUpdateStructure(persona);
        controller.refresh();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        ValidatorTextField validator = new ValidatorTextField();
        validator.validate(tfFamily, 20, true, false, false, false);
        validator.validate(tfName, 15, true, false, false, false);
        validator.validate(tfLastname, 20, true, false, false, false);
        validator.validate(tfPosition, 100, true, false, true, true);
        validator.validate(tfPassword, 8, false, true, true, false);

        if (persona.getId() != 0L) { //режим редактирования
            tfFamily.setText(persona.getFamily());
            tfName.setText(persona.getName());
            tfLastname.setText(persona.getLastname());
            tfPosition.setText(persona.getPosition());
            tfPassword.setText(persona.getPassword());
        }
        //кнопка сохранить активна только если все обязательные поля заполнены
        btnSave.disableProperty().bind(
                Bindings.isEmpty(tfFamily.textProperty())
                        .or(Bindings.isEmpty(tfName.textProperty()))
                        .or(Bindings.isEmpty(tfLastname.textProperty()))
                        .or(Bindings.isEmpty(tfPosition.textProperty()))
        );
    }
}
