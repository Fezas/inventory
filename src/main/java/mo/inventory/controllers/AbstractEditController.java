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
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class AbstractEditController implements Initializable {
    private AbstractActive active;
    private AbstractController controller;

    @FXML    private Button btnCancel;
    @FXML    private Button btnSave;
    @FXML    private Button btnSelectIcon;
    @FXML    private ComboBox<?> cmbBoxCategory;
    @FXML    private ComboBox<?> cmbBoxOkei;
    @FXML    private ComboBox<?> cmbBoxType;
    @FXML    private TextField tfTitle, tfPrice0;
    @FXML    private TextArea taNote;

    public AbstractEditController(AbstractActive active) {
        this.active = active;
    }


    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {

    }

    public void setParent (AbstractController controller){
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setGraphic(new FontIcon("anto-save"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        if (active.getId() != 0L) { //режим редактирования
            tfTitle.setText(active.getTitle());
            taNote.setText(active.getNote());
            tfPrice0.setText(active.getPrice0().toString());
        }
    }
}
