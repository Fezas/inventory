/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.entity.AbstractActive;
import mo.inventory.entity.CategoryActive;
import mo.inventory.model.AbstractActiveModel;
import mo.inventory.model.CategoryActiveModel;
import mo.inventory.model.HelpModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AbstractController implements Initializable {
    @FXML    private AnchorPane anchorPane;
    private TabPane tabPane = new TabPane();
    private Button btnAdd = new Button();
    private Button btnEdit = new Button();
    private Button btnCopy = new Button();
    private Button btnDelete = new Button();
    private Button btnHelp = new Button();
    private TableView<AbstractActive> currentTable;
    private CategoryActiveModel categoryActiveModel = new CategoryActiveModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<CategoryActive> categories = categoryActiveModel.getAll();
        createButton();
        for (CategoryActive category : categories) {
            //Создаем вкладки
            Tab tab = new Tab();
            tab.setText(category.getTitle());
            tabPane.getTabs().add(tab);
            //создаем таблицы для хранения абстрактных активов
            TableView<AbstractActive> tableView = new TableView<AbstractActive>();
            createTable(tableView);
            Insets insets = new Insets(0);
            tableView.setPadding(insets);
            tab.setContent(tableView);
            anchorPane.setTopAnchor(tabPane, 40.0);
            anchorPane.setBottomAnchor(tabPane, 10.0);
            anchorPane.setLeftAnchor(tabPane, 10.0);
            anchorPane.setRightAnchor(tabPane, 10.0);
            ObservableList<AbstractActive> data = FXCollections.observableArrayList();
            //System.out.println(category.getAbstractActives().size());
            data.addAll(category.getAbstractActives());
            tableView.setItems(data);
        }

    }

    private void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText(null);
        alert.setContentText(HelpModel.getFromId("abstract_active"));
        alert.showAndWait();
    }

    private void createButton() {
        Tooltip tooltipAdd = new Tooltip();
        tooltipAdd.setText("Добавить");
        Tooltip tooltipEdit = new Tooltip();
        tooltipEdit.setText("Редактировать");
        Tooltip tooltipCopy = new Tooltip();
        tooltipCopy.setText("Копировать");
        Tooltip tooltipDelete = new Tooltip();
        tooltipDelete.setText("Удалить");
        Tooltip tooltipHelp = new Tooltip();
        tooltipHelp.setText("Справка");

        btnAdd.setGraphic(new FontIcon("anto-plus-circle:15"));
        btnEdit.setGraphic(new FontIcon("anto-edit:15"));
        btnCopy.setGraphic(new FontIcon("anto-copy:15"));
        btnDelete.setGraphic(new FontIcon("anto-delete:15"));
        btnHelp.setGraphic(new FontIcon("anto-question-circle:15"));

        btnAdd.setTooltip(tooltipAdd);
        btnEdit.setTooltip(tooltipEdit);
        btnCopy.setTooltip(tooltipCopy);
        btnDelete.setTooltip(tooltipDelete);
        btnHelp.setTooltip(tooltipHelp);

        btnHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                help(e);
            }
        });

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                edit("Добавление актива", new AbstractActive());
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                delete();
            }
        });

        anchorPane.getChildren().add(tabPane);
        anchorPane.getChildren().add(btnAdd);
        anchorPane.getChildren().add(btnEdit);
        anchorPane.getChildren().add(btnCopy);
        anchorPane.getChildren().add(btnDelete);
        anchorPane.getChildren().add(btnHelp);

        anchorPane.setTopAnchor(btnHelp, 10.0);
        anchorPane.setRightAnchor(btnHelp, 180.0);
        anchorPane.setTopAnchor(btnAdd, 10.0);
        anchorPane.setRightAnchor(btnAdd, 140.0);
        anchorPane.setTopAnchor(btnEdit, 10.0);
        anchorPane.setRightAnchor(btnEdit, 100.0);
        anchorPane.setTopAnchor(btnCopy, 10.0);
        anchorPane.setRightAnchor(btnCopy, 60.0);
        anchorPane.setTopAnchor(btnDelete, 10.0);
        anchorPane.setRightAnchor(btnDelete, 20.0);
    }

    private void createTable(TableView tableView) {
        TableColumn<AbstractActive, String> clmnRemark = new TableColumn<>();
        TableColumn<AbstractActive, String> clmnIcon = new TableColumn<>();
        TableColumn<AbstractActive, String> clmnTitle = new TableColumn<>();
        TableColumn<AbstractActive, String> clmnOkei = new TableColumn<>();
        TableColumn<AbstractActive, String> clmnTypeActive = new TableColumn<>();
        TableColumn<AbstractActive, BigDecimal> clmnPrice0 = new TableColumn<>(); //балансовая стоимость
        TableColumn<AbstractActive, String> clmnNote = new TableColumn<>();

        // устанавливаем тип и значение которое должно хранится в колонке
        clmnRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));
        clmnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        clmnOkei.setCellValueFactory(new PropertyValueFactory<>("okei"));
        clmnPrice0.setCellValueFactory(new PropertyValueFactory<>("price0"));
        clmnNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        clmnTitle.setText("Наименование");
        clmnOkei.setText("Ед.изм.");
        clmnTypeActive.setText("Тип");
        clmnPrice0.setText("Баланс. стоим.");
        clmnNote.setText("Примечание");

        clmnRemark.setPrefWidth(25);
        clmnIcon.setPrefWidth(25);
        clmnTitle.setPrefWidth(210);
        clmnOkei.setPrefWidth(55);
        clmnTypeActive.setPrefWidth(140);
        clmnPrice0.setPrefWidth(100);
        clmnNote.setPrefWidth(250);

        clmnRemark.setResizable(false);
        clmnIcon.setResizable(false);
        clmnTitle.setResizable(false);
        clmnOkei.setResizable(false);
        clmnTypeActive.setResizable(false);
        clmnPrice0.setResizable(false);
        clmnNote.setResizable(false);

        tableView.getColumns().add(clmnRemark);
        tableView.getColumns().add(clmnIcon);
        tableView.getColumns().add(clmnTitle);
        tableView.getColumns().add(clmnOkei);
        tableView.getColumns().add(clmnTypeActive);
        tableView.getColumns().add(clmnPrice0);
        tableView.getColumns().add(clmnNote);
    }

    private void delete() {
        ObservableList<AbstractActive> items = currentTable.getItems();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить выбранные записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            for (AbstractActive active : items) {
                if (active.getRemark().isSelected()) {
                    if (option.get() == ButtonType.OK) {
                        AbstractActiveModel.delete(active);
                        //refresh();
                    }
                }
            }
        }

    }

    private void edit(String title, AbstractActive active) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/abstract-active-edit.fxml"));
            AbstractEditController abstractEditController = new AbstractEditController(active);
            fxmlLoader.setController(abstractEditController);
            AbstractEditController c = fxmlLoader.getController();
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
