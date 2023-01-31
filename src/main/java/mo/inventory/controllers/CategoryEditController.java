/*
 * Copyright (c) 2022. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import mo.inventory.entity.CategoryActive;
import mo.inventory.model.CategoryActiveModel;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryEditController implements Initializable {
    private CategoryActive category;
    private long id;
    private CategoryActiveController controller;

    public CategoryEditController(CategoryActive category, long id) {
        this.category = category;
        this.id = id;
    }

    @FXML    private Button btnCancel;
    @FXML    private Button btnSave;
    @FXML    private TextField tfTitle;

    public TreeTableView<CategoryActive> tblCategorySecond;

    public void setParent (CategoryActiveController controller){
        this.controller = controller;
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        CategoryActiveModel model = new CategoryActiveModel();
        category.setTitle(tfTitle.getText());
        if (category.getId() == 0L) { //создание нового узла
            category.setIdCategory(id);
        }
        model.saveOrUpdateCategory(category);
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

        if (category.getId() != 0L) { //режим редактирования
            tfTitle.setText(category.getTitle());
        }
    }
}
