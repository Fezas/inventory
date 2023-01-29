/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.entity.StatusActive;
import mo.inventory.model.HelpModel;
import mo.inventory.model.StatusActiveModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class StatusController implements Initializable {
    private static ObservableList<StatusActive> basic = FXCollections.observableArrayList();
    private static ObservableList<StatusActive> material = FXCollections.observableArrayList();
    public static StatusActive selected;

    @FXML    private Button btnCancel, btnHelp, btnAdd;

    @FXML    private TableColumn<StatusActive, String> clmnCodeBasic;
    @FXML    private TableColumn<StatusActive, String> clmnCodeMaterial;

    @FXML    private TableColumn<StatusActive, String> clmnTitleBasic;
    @FXML    private TableColumn<StatusActive, String> clmnTitleMaterial;

    @FXML    private TableView<StatusActive> tblBasic;
    @FXML    private TableView<StatusActive> tblMaterial;

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText(null);
        alert.setContentText(HelpModel.getFromId("type_active"));
        alert.showAndWait();
    }

    @FXML
    void add(ActionEvent event) {
        edit("Добавление нового статуса", new StatusActive());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnCancel.setGraphic(new FontIcon("anto-close"));
        btnHelp.setGraphic(new FontIcon("anto-question-circle"));
        btnAdd.setGraphic(new FontIcon("anto-plus-circle"));
        init();
        // контексное меню таблицы
        rowFactory(tblBasic, basic, "01");
        rowFactory(tblMaterial, material, "02");
        // устанавливаем тип и значение которое должно хранится в колонке
        clmnCodeBasic.setCellValueFactory(new PropertyValueFactory<StatusActive, String>("code"));
        clmnTitleBasic.setCellValueFactory(new PropertyValueFactory<StatusActive, String>("titleStatusActive"));
        clmnCodeMaterial.setCellValueFactory(new PropertyValueFactory<StatusActive, String>("code"));
        clmnTitleMaterial.setCellValueFactory(new PropertyValueFactory<StatusActive, String>("titleStatusActive"));
        // заполняем таблицу данными
        tblBasic.setItems(basic);
        tblMaterial.setItems(material);
    }

    public void init() {
        initData("01");
        initData("02");
    }

    private void initData(String typeCode) {
        ObservableList<StatusActive> list;
        if (typeCode.equals("01")) list = basic;
                else list = material;
        list.clear();
        var statuses = StatusActiveModel.getListStatusesActiveFromType(typeCode);
        for (StatusActive status : statuses) {
            list.add(new StatusActive(
                    status.getCode(),
                    status.getTitleStatusActive(),
                    status.getTypeObject(),
                    status.getIcon()
            ));
        }
    }

    private void rowFactory(TableView<StatusActive> table, ObservableList<StatusActive> list, String typeCode) {
        table.setRowFactory (
                tableView -> {
                    //событие по двойному клику строки
                    final TableRow<StatusActive> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            StatusActive rowData = row.getItem();
                        }
                    });
                    //контексное меню
                    final ContextMenu rowMenu = new ContextMenu();

                    MenuItem editItem = new MenuItem("Редактировать");
                    MenuItem removeItem = new MenuItem("Удалить пункт");

                    editItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            selected = row.getItem();
                            edit(row.getItem().getTitleStatusActive(), row.getItem());
                            //actionEditAddress(selectAddress);
                            selected = null;
                        }
                    });

                    removeItem.setOnAction(event -> {
                        Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                        alertDelete.setTitle("Внимание");
                        alertDelete.setHeaderText("Удаление записи");
                        alertDelete.setContentText("Удалить запись: " + row.getItem().getTitleStatusActive() + "?");
                        Optional<ButtonType> option = alertDelete.showAndWait();
                        if (option.get() == null) {

                        } else if (option.get() == ButtonType.OK) {
                            StatusActiveModel.delete(row.getItem());
                            table.getItems().remove(row.getItem());
                            initData(typeCode);
                        } else if (option.get() == ButtonType.CANCEL) {

                        }
                    });
                    rowMenu.getItems().addAll(editItem, removeItem);
                    // only display context menu for non-empty rows:
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(rowMenu));
                    return row;
                }
        );
    }

    public void edit(String title, StatusActive status) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/active-status-edit.fxml"));
            StatusEditController statusEditController = new StatusEditController(status);
            fxmlLoader.setController(statusEditController);
            StatusEditController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
