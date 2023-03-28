/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.dto.MetallDTO;
import mo.inventory.entity.*;
import mo.inventory.model.*;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AbstractEditController implements Initializable {
    private AbstractActive active;
    private AbstractController controller;
    private FixAbstractController fixAbstractController;
    public TableRow<MetallDTO> currentRow;
    private static ObservableList<MetallDTO> metalls = FXCollections.observableArrayList();
    @FXML
    private Button btnCancel, btnSave, btnSelectIcon, btnHelp;
    @FXML    private ComboBox<CategoryActive> cmbBoxCategory;
    @FXML    private ComboBox<Okei> cmbBoxOkei;
    @FXML    private ComboBox<TypeActive> cmbBoxType;
    @FXML    private TextField tfTitle, tfPrice0;
    @FXML    private TextArea taNote;
    @FXML    private TableColumn<MetallDTO, String> clmnMetallCode;
    @FXML    private TableColumn<MetallDTO, String> clmnMetallTitle;
    @FXML    private TableColumn<MetallDTO, String> clmnMetallWeight;
    @FXML    private TableView<MetallDTO> tblMetall;

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
        for (MetallDTO metal : metalls) { // сохраняем металлы
            if (metal.getWeight() != null) {
                MetallInActive metallInActive = new MetallInActive();
                metallInActive.setCodeMetall(metal.getCode());
                metallInActive.setIdAbstractActive(active.getId());
                metallInActive.setWeight(Double.parseDouble(metal.getWeight()));
                MetalInActiveModel.saveOrUpdateStructure(metallInActive);
            }
        }
        controller.refresh();
        cancel();
    }

    @FXML
    void help(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Справка");
        alert.setHeaderText(null);
        alert.setContentText(HelpModel.getFromId("abstract_active_edit"));
        alert.showAndWait();
    }

    public void setParent (AbstractController controller){
        this.controller = controller;
    }
    public void setParentFix (FixAbstractController fixAbstractController){
        this.fixAbstractController = fixAbstractController;
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

        // устанавливаем тип и значение которое должно хранится в колонке

        clmnMetallTitle.setCellValueFactory(new PropertyValueFactory<MetallDTO, String>("nameMetall"));
        clmnMetallCode.setCellValueFactory(new PropertyValueFactory<MetallDTO, String>("code"));
        clmnMetallWeight.setCellValueFactory(new PropertyValueFactory<MetallDTO, String>("weight"));

        tblMetall.setEditable(true);

        // заполняем таблицу данными
        initMetallsDTO();
        tblMetall.setItems(metalls);
        weightMetallInActive();
        tblMetall.setRowFactory(
                table -> {
                    //событие по клику строки
                    final TableRow<MetallDTO> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                            currentRow = row;
                            edit(currentRow);
                        }
                    });
                    return row;
                }
        );
    }

    public void initMetallsDTO() {
        metalls.clear();
        List<Metall> metallsList = MetallModel.getAll();
        for (Metall metall : metallsList) {
            MetallDTO metallDTO = new MetallDTO(
                    metall.getCode(),
                    metall.getNameMetall(),
                    metall.getShortNameMetall(),
                    null
            );
            metalls.add(metallDTO);
        }
    }

    private void weightMetallInActive() {
        List<MetallInActive> metallsList = MetalInActiveModel.getFromMetallWithActive(active.getId());
        for (MetallInActive metallInActive : metallsList) {
            for (MetallDTO metal : metalls) {
                if (metal.getCode().equals(metallInActive.getCodeMetall())) {
                    metal.setWeight(Double.toString(metallInActive.getWeight()));
                }
            }
        }
    }

    private void edit(TableRow<MetallDTO> row) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/input-metall-weight.fxml"));
            InputMetallWeightController inputMetallWeightController = new InputMetallWeightController(row);
            fxmlLoader.setController(inputMetallWeightController);
            InputMetallWeightController c = fxmlLoader.getController();
            c.setParent(this);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(currentRow.getItem().getNameMetall());
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
