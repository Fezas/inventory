/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.entity.*;
import mo.inventory.model.*;
import mo.inventory.util.ValidatorTextField;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class FixAbstractController implements Initializable {
    private Persona persona;
    private AbstractActive changedAbstractActive;
    private Map<CategoryActive, TableView> mapData = new HashMap<>();
    private AbstractActive select;
    private TabPane tabPane = new TabPane();
    private Button btnAdd = new Button();
    //private Button btnEdit = new Button();
    private Button btnCopy = new Button();
    private Button btnDelete = new Button();
    private Button btnHelp = new Button();
    private TableView<AbstractActive> currentTable;
    private CategoryActiveModel categoryActiveModel = new CategoryActiveModel();
    private MainController mainController;
    private Active active;
    @FXML    private AnchorPane anchorPane;
    @FXML    private Pane paneTable;
    @FXML    private Button btnCancel, btnSave;
    @FXML    private ComboBox<FunctionActive> cmbFunctionActive;
    @FXML    private ComboBox<Provider> cmbProvider;
    @FXML    private ComboBox<StatusActive> cmbStatusActive;
    @FXML    private DatePicker dateAccounting, dateCommissioning;
    @FXML    private Label lblNameMol, lblActiveTitle;
    @FXML    private TextField tfAccountNumber, tfAmount, tfFactoryNumber, tfInventoryNumber;
    @FXML    private CheckComboBox<?> chkComboBoxClmnVisible;

    public FixAbstractController(Persona persona, Active active) {
        this.persona = persona;
        this.active = active;
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        System.out.println(changedAbstractActive.getTitle());
        active.setAbstractActive(changedAbstractActive);
        active.setAmount(Double.parseDouble(tfAmount.getText()));
        if (!tfAccountNumber.getText().isEmpty()) {
            active.setAccountNumber(tfAccountNumber.getText());
        }
        if (!tfFactoryNumber.getText().isEmpty()) {
            active.setFactoryNumber(tfFactoryNumber.getText());
        }
        if (!tfInventoryNumber.getText().isEmpty()) {
            active.setInventoryNumber(tfInventoryNumber.getText());
        }
        active.setPersona(persona);
        active.setProvider(cmbProvider.getSelectionModel().getSelectedItem());
        active.setFunctionActive(cmbFunctionActive.getSelectionModel().getSelectedItem());
        active.setStatusActive(cmbStatusActive.getSelectionModel().getSelectedItem());
        active.setDateRecordCreation(Timestamp.valueOf(LocalDateTime.now()));
        ActiveModel.saveOrUpdateStructure(active);
        tfInventoryNumber.clear();
        tfFactoryNumber.clear();
        tfAccountNumber.clear();
        tfAmount.clear();
        active = new Active();
        mainController.refreshMainTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSave.setGraphic(new FontIcon("anto-pushpin"));
        btnCancel.setGraphic(new FontIcon("anto-close"));
        lblNameMol.setText(persona.getFamily() + " " + persona.getName().charAt(0) + "." + persona.getLastname().charAt(0)); //титул FIO
        cmbProvider.getItems().addAll(ProviderModel.getAll());
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
            anchorPane.setTopAnchor(tabPane, 50.0);
            anchorPane.setBottomAnchor(tabPane, 10.0);
            anchorPane.setLeftAnchor(tabPane, 10.0);
            anchorPane.setRightAnchor(tabPane, 350.0);
            ObservableList<AbstractActive> data = FXCollections.observableArrayList();
            data.addAll(category.getAbstractActives());
            tableView.getItems().addAll(data);
            mapData.put(category, tableView);
        }
        validate();


    }

    private void validate() {
        ValidatorTextField validator = new ValidatorTextField();
        validator.validate(tfAmount,10, false, false, true, false);
    }

    public void refresh() {
        for (Map.Entry<CategoryActive, TableView> entry : mapData.entrySet()) {
            ObservableList<AbstractActive> data = FXCollections.observableArrayList();
            data.addAll(CategoryActiveModel.getFromId(entry.getKey().getId()).getAbstractActives());
            entry.getValue().setItems(data);
        }
    }

    public void setParentFix (MainController mainController){
        this.mainController = mainController;
    }

    private void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText(null);
        alert.setContentText(HelpModel.getFromId("fix_abstract_active"));
        alert.showAndWait();
    }

    private void createButton() {
        Tooltip tooltipAdd = new Tooltip();
        tooltipAdd.setText("Добавить");
        //Tooltip tooltipEdit = new Tooltip();
        //tooltipEdit.setText("Редактировать");
        Tooltip tooltipCopy = new Tooltip();
        tooltipCopy.setText("Копировать");
        Tooltip tooltipDelete = new Tooltip();
        tooltipDelete.setText("Удалить");
        Tooltip tooltipHelp = new Tooltip();
        tooltipHelp.setText("Справка");

        btnAdd.setGraphic(new FontIcon("anto-plus-circle:15"));
        //btnEdit.setGraphic(new FontIcon("anto-edit:15"));
        btnCopy.setGraphic(new FontIcon("anto-copy:15"));
        btnDelete.setGraphic(new FontIcon("anto-delete:15"));
        btnHelp.setGraphic(new FontIcon("anto-question-circle:15"));

        btnAdd.setTooltip(tooltipAdd);
        //btnEdit.setTooltip(tooltipEdit);
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
        //anchorPane.getChildren().add(btnEdit);
        anchorPane.getChildren().add(btnCopy);
        anchorPane.getChildren().add(btnDelete);
        anchorPane.getChildren().add(btnHelp);

        anchorPane.setTopAnchor(btnHelp, 12.0);
        anchorPane.setRightAnchor(btnHelp, 470.0);
        anchorPane.setTopAnchor(btnAdd, 12.0);
        anchorPane.setRightAnchor(btnAdd, 430.0);
        //anchorPane.setTopAnchor(btnEdit, 10.0);
        //anchorPane.setRightAnchor(btnEdit, 100.0);
        anchorPane.setTopAnchor(btnCopy, 12.0);
        anchorPane.setRightAnchor(btnCopy, 390.0);
        anchorPane.setTopAnchor(btnDelete, 12.0);
        anchorPane.setRightAnchor(btnDelete, 350.0);
    }

    private void createTable(TableView tableView) {
        TableColumn<AbstractActive, String> clmnIcon = new TableColumn<>();
        TableColumn<AbstractActive, String> clmnTitle = new TableColumn<>("Наименование");
        TableColumn<AbstractActive, String> clmnOkei = new TableColumn<>("Ед.изм.");
        TableColumn<AbstractActive, String> clmnTypeActive = new TableColumn<>("Тип");
        TableColumn<AbstractActive, BigDecimal> clmnPrice0 = new TableColumn<>("Баланс. стоим."); //балансовая стоимость
        TableColumn<AbstractActive, String> clmnNote = new TableColumn<>("Примечание");

        // устанавливаем тип и значение которое должно хранится в колонке
        clmnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        clmnOkei.setCellValueFactory(new PropertyValueFactory<>("okei"));
        clmnPrice0.setCellValueFactory(new PropertyValueFactory<>("price0"));
        clmnNote.setCellValueFactory(new PropertyValueFactory<>("note"));


        tableView.setRowFactory(
                table -> {
                    //событие по клику строки
                    final TableRow<AbstractActive> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                            AbstractActive selectActive = row.getItem();
                        }
                    });
                    //контексное меню
                    final ContextMenu rowMenu = new ContextMenu();

                    MenuItem editItem = new MenuItem("Редактировать");
                    MenuItem removeItem = new MenuItem("Удалить пункт");
                    MenuItem fixItem = new MenuItem("Закрепить");

                    editItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            select = row.getItem();
                            edit("Редактирование записи", select);
                            select = null;
                        }
                    });
                    removeItem.setOnAction(event -> {
                        Alert alertDelete = new Alert(Alert.AlertType.CONFIRMATION);
                        alertDelete.setTitle("Внимание");
                        alertDelete.setHeaderText("Удаление записи");
                        alertDelete.setContentText("Удалить запись: " + row.getItem().getTitle() + "?");
                        Optional<ButtonType> option = alertDelete.showAndWait();
                        if (option.get() == null) {

                        } else if (option.get() == ButtonType.OK) {
                            AbstractActiveModel.delete(row.getItem());
                            refresh();
                        } else if (option.get() == ButtonType.CANCEL) {

                        }
                    });
                    fixItem.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            select = row.getItem();
                            fix("Закрепление " + select.getTitle(), select);
                            select = null;
                        }
                    });

                    rowMenu.getItems().addAll(editItem, removeItem, fixItem);
                    // only display context menu for non-empty rows:
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu) null)
                                    .otherwise(rowMenu));
                    return row;
                }
        );

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(tableView.getSelectionModel().getSelectedItem() != null)
                {
                    changedAbstractActive = (AbstractActive) tableView.getSelectionModel().getSelectedItem();
                    cmbStatusActive.getItems().addAll(StatusActiveModel.getListStatusesActiveFromType(changedAbstractActive.getTypeObject().getCode()));
                    cmbFunctionActive.getItems().addAll(FunctionActiveModel.getListFunctionsActiveFromType(changedAbstractActive.getTypeObject().getCode()));
                    System.out.println();
                    lblActiveTitle.setText(changedAbstractActive.getTitle());
                    tfAccountNumber.setDisable(false);
                    tfAmount.setDisable(false);
                    tfFactoryNumber.setDisable(false);
                    tfInventoryNumber.setDisable(false);
                    cmbFunctionActive.setDisable(false);
                    cmbProvider.setDisable(false);
                    cmbStatusActive.setDisable(false);
                    dateAccounting.setDisable(false);
                    dateCommissioning.setDisable(false);
                }
            }
        });
        clmnIcon.setPrefWidth(25);
        clmnTitle.setPrefWidth(210);
        clmnOkei.setPrefWidth(55);
        clmnTypeActive.setPrefWidth(100);
        clmnPrice0.setPrefWidth(100);
        clmnNote.setPrefWidth(174);

        clmnIcon.setResizable(false);
        clmnTitle.setResizable(false);
        clmnOkei.setResizable(false);
        clmnTypeActive.setResizable(false);
        clmnPrice0.setResizable(false);
        clmnNote.setResizable(false);

        tableView.getColumns().add(clmnIcon);
        tableView.getColumns().add(clmnTitle);
        tableView.getColumns().add(clmnOkei);
        tableView.getColumns().add(clmnTypeActive);
        tableView.getColumns().add(clmnPrice0);
        tableView.getColumns().add(clmnNote);
    }

    private void delete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить выбранные записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            for (Map.Entry<CategoryActive, TableView> entry : mapData.entrySet()) {
                ObservableList<AbstractActive> items = entry.getValue().getItems();
                for (AbstractActive active : items) {
                    if (active.getRemark().isSelected()) {
                        if (option.get() == ButtonType.OK) {
                            AbstractActiveModel.delete(active);
                            refresh();
                        }
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
            c.setParentFix(this);
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

    private void fix(String title, AbstractActive active) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/fix-person-tree.fxml"));
            SelectPersonController selectPersonController = new SelectPersonController(active);
            fxmlLoader.setController(selectPersonController);
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
