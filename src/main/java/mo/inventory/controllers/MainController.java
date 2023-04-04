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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.dto.StateDTO;
import mo.inventory.entity.Active;
import mo.inventory.entity.Persona;
import mo.inventory.entity.SettingMainTable;
import mo.inventory.entity.Structure;
import mo.inventory.model.ActiveModel;
import mo.inventory.model.PersonaModel;
import mo.inventory.model.SettingMainTableModel;
import mo.inventory.model.StructureModel;
import mo.inventory.util.ContextMenuListCell;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckTreeView;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private StateDTO node;
    public Persona currentPersona;
    private TableColumn<Active, String> clmnMol = new TableColumn<>("МОЛ");
    private TableColumn<Active, String> clmnActive = new TableColumn<>("Актив");
    private TableColumn<Active, String> clmnAmount = new TableColumn<>("Количество");
    private TableColumn<Active, String> clmnAccNumb = new TableColumn<>("№ бух. уч.");
    private TableColumn<Active, String> clmnFactNumb = new TableColumn<>("№ зав.");
    private TableColumn<Active, String> clmnInvNumb = new TableColumn<>("№ инв.");
    private TableColumn<Active, String> clmnDateComiss = new TableColumn<>("Дата ввода");
    private TableColumn<Active, String> clmnDateAcc = new TableColumn<>("Дата постановки");
    private TableColumn<Active, String> clmnFunc = new TableColumn<>("Функция");
    private TableColumn<Active, String> clmnStat = new TableColumn<>("Статус");
    private TableColumn<Active, String> clmnProv = new TableColumn<>("Поставщик");
    private ObservableList<Active> dataMainTable = FXCollections.observableArrayList();
    @FXML    private CheckTreeView<StateDTO> checkTreeViewStructure;
    @FXML    private CheckComboBox<String> checkBoxCategory;
    @FXML    private Button btnStructure;
    @FXML    private TableView<Active> mainTable;
    @FXML    private TabPane tabPaneOutput;
    @FXML    private Tab tabAll;


    public Persona getCurrentPersona() {
        return currentPersona;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActiveModel.getAll();
        createStructure();
        checkTreeViewStructure.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //checkTreeViewStructure.setShowRoot(false);

        // Создание MenuItem и помещение его в ContextMenu
        MenuItem addFix = new MenuItem("Закрепление");
        ContextMenu contextMenu = new ContextMenu(addFix);
        addFix.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("addFix " + checkTreeViewStructure.getSelectionModel().getSelectedItem().getValue().getTitle());
                StateDTO selectItem = checkTreeViewStructure.getSelectionModel().getSelectedItem().getValue();
                //System.out.println(PersonaModel.getFromId(selectItem.getIdState()).getFamily());
                if(!selectItem.isType()) { //если выбранный узел - персона
                    currentPersona = PersonaModel.getFromId(selectItem.getIdState());
                    createSceneFix(currentPersona, new Active());
                }
            }
        });

        // устанавливает фабрику ячеек в ListView, сообщая ему об использовании ранее созданного ContextMenu (использует фабрику ячеек по умолчанию)
        checkTreeViewStructure.setCellFactory(ContextMenuListCell.<StateDTO>forListView(contextMenu));

        // То же, что и выше, но использует пользовательскую фабрику ячеек, которая определена в другом месте.
        // checkTreeViewStructure.setCellFactory(ContextMenuListCell.<StateDTO>forListView(contextMenu, customCellFactory));

        //создаем таблицу вывода данных
        createMainTable();
    }

    private void createMainTable() {
        mainTable = new TableView<>();

        mainTable.getColumns().addAll(clmnActive, clmnMol, clmnAmount, clmnAccNumb, clmnFactNumb, clmnInvNumb,
                clmnDateComiss, clmnDateAcc, clmnFunc, clmnStat, clmnProv);
        tabAll.setContent(mainTable);
        loadSettingVisibleColumn();
        initColumnMainTable();
        refreshMainTable();
        //clmnTitle.setCellValueFactory(p -> p.getValue().getAbstractActive().getTitle());
    }

    public void refreshMainTable() {
        if (!dataMainTable.isEmpty()) dataMainTable.clear();
        dataMainTable.addAll(ActiveModel.getAll());
        mainTable.getItems().addAll(dataMainTable);
    }

    private void initColumnMainTable() {
        clmnActive.setCellValueFactory(new PropertyValueFactory<>("abstractActive"));
        clmnMol.setCellValueFactory(new PropertyValueFactory<>("persona"));
        clmnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        clmnAccNumb.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        clmnFactNumb.setCellValueFactory(new PropertyValueFactory<>("factoryNumber"));
        clmnInvNumb.setCellValueFactory(new PropertyValueFactory<>("inventoryNumber"));
        clmnDateAcc.setCellValueFactory(new PropertyValueFactory<>("dateAccounting"));
        clmnDateComiss.setCellValueFactory(new PropertyValueFactory<>("dateComissioning"));
        clmnFunc.setCellValueFactory(new PropertyValueFactory<>("functionActive"));
        clmnStat.setCellValueFactory(new PropertyValueFactory<>("statusActive"));
        clmnProv.setCellValueFactory(new PropertyValueFactory<>("provider"));
    }

    private void loadSettingVisibleColumn() {
        //загружаем настройки видимости столбцов для текущего пользователя
        SettingMainTable settingVisibleClmnMainTable = SettingMainTableModel.getFromId(1L);
        clmnActive.setVisible(settingVisibleClmnMainTable.isClmnTitle());
        clmnMol.setVisible(settingVisibleClmnMainTable.isClmnMol());
        clmnAmount.setVisible(settingVisibleClmnMainTable.isClmnAmount());
        clmnAccNumb.setVisible(settingVisibleClmnMainTable.isClmnAccountNumber());
        clmnFactNumb.setVisible(settingVisibleClmnMainTable.isClmnFactoryNumber());
        clmnInvNumb.setVisible(settingVisibleClmnMainTable.isClmnInventoryNumber());
        clmnDateAcc.setVisible(settingVisibleClmnMainTable.isClmnDateAccounting());
        clmnDateComiss.setVisible(settingVisibleClmnMainTable.isClmnDateComissions());
        clmnFunc.setVisible(settingVisibleClmnMainTable.isClmnFunctionActive());
        clmnStat.setVisible(settingVisibleClmnMainTable.isClmnStatusActive());
        clmnProv.setVisible(settingVisibleClmnMainTable.isClmnProvider());
    }

    public void createStructure() {
        Structure root = StructureModel.getRootStructure();
        StateDTO rt = createNode(root);
        rt.getBtnDelete().setVisible(false);
        CheckBoxTreeItem<StateDTO> itemRoot = new CheckBoxTreeItem<StateDTO>(rt); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        checkTreeViewStructure.setRoot(itemRoot);
    }

    /**
     * Функция создания узла штата {@link Structure} в структуре типа {@link TreeView}
     * @return возвращает узел {@link StateDTO}
     */

    private StateDTO createNode(Structure structure) {
        node = new StateDTO(structure.getTitle().toUpperCase(Locale.ROOT), structure.getId(), true);
        return node;
    }

    /**
     * Функция рекурсивного формирования {@link TreeView} с помощью запроса  {@link StructureModel#getFromIdStructure(long)}
     */

    public void structure(CheckBoxTreeItem<StateDTO> itemRoot) {
        List<Structure> data = StructureModel.getFromIdStructure(itemRoot.getValue().getIdState()); //дочерние узлы
        List<Persona> persons = PersonaModel.getFromIdStructure(itemRoot.getValue().getIdState()); //персоны в узлах
        if (!persons.isEmpty()) {
            for (Persona persona : persons) {
                String fio = persona.getFamily() + " " + persona.getName().charAt(0) + "." + persona.getLastname().charAt(0) + ".";
                StateDTO node = new StateDTO(persona.getPosition() + " " + fio, persona.getId(), false);
                CheckBoxTreeItem<StateDTO> itemPersona = new CheckBoxTreeItem<StateDTO>(node);
                itemPersona.getValue().getBtnAddNode().setVisible(false);
                itemPersona.getValue().getBtnAddPersona().setVisible(false);
                itemRoot.getChildren().add(itemPersona);
            }
        }
        if (!data.isEmpty()) {
            for (Structure structure : data) {
                CheckBoxTreeItem<StateDTO> item = new CheckBoxTreeItem<StateDTO>(createNode(structure));
                item.setExpanded(true);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    public void refresh() {
        checkTreeViewStructure.getRoot().getChildren().clear();
        structure((CheckBoxTreeItem<StateDTO>) checkTreeViewStructure.getRoot());
    }

    private void createScene(String nameResourceXML, String title, String css, Boolean resizable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + nameResourceXML));
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(loader.load());
            if (nameResourceXML.equals("structure-table.fxml")){ // передаем контроллер для обновления checkTreeViewStructure
                StructureController structureController = loader.getController();
                structureController.setParent(this);
            }
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("/css/" + css).toString());
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/telegra.png"))));
            stage.setResizable(resizable);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    private void createSceneFix(Persona persona, Active active) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/fix-abstract-active-table.fxml"));
            FixAbstractController fixAbstractController = new FixAbstractController(persona, active);
            loader.setController(fixAbstractController);
            Stage stage = new Stage();
            stage.setTitle("Закрепление");
            Scene scene = new Scene(loader.load());
            FixAbstractController controller = loader.getController();
            controller.setParentFix(this);
            stage.setScene(scene);
            //scene.getStylesheets().add(getClass().getResource("/css/" + css).toString());
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/telegra.png"))));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (NullPointerException e) {
            e.printStackTrace();
            //logger.error("Error", e);
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
            //logger.error("Error", e);
        }
    }

    @FXML
    void structureAction(ActionEvent event) {
        createScene("structure-table.fxml", "Структура", "structure-table.css", false);
    }
    @FXML
    void statusAction(ActionEvent event) {
        createScene("active-status.fxml", "Статусы", "", false);
    }
    @FXML
    void functionAction(ActionEvent event) {
        createScene("active-function.fxml", "Функции", "", false);
    }
    @FXML
    void categoryAction(ActionEvent event) {
        createScene("category-table.fxml", "Категории", "structure-table.css", false);
    }
    @FXML
    void abstractAction(ActionEvent event) {
        createScene("abstract-active-table.fxml", "Ценности", "", false);
    }
    @FXML
    void fixAction(ActionEvent event) {
        createScene("active-fix.fxml", "Ценности", "", false);
    }


}
