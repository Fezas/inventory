/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckTreeView;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private StateDTO node;
    public Persona currentPersona;
    private TableColumn<Active, String> clmnMol = new TableColumn<>("МОЛ");
    private TableColumn<Active, String> clmnNewMol = new TableColumn<>("МОЛ");
    private TableColumn<Active, String> clmnActive = new TableColumn<>("Актив");
    private TableColumn<Active, String> clmnAmount = new TableColumn<>("Кол-во");
    private TableColumn<Active, String> clmnAccNumb = new TableColumn<>("№ бух. уч.");
    private TableColumn<Active, String> clmnFactNumb = new TableColumn<>("№ зав.");
    private TableColumn<Active, String> clmnInvNumb = new TableColumn<>("№ инв.");
    private TableColumn<Active, String> clmnDateComiss = new TableColumn<>("Дата ввода");
    private TableColumn<Active, String> clmnDateAcc = new TableColumn<>("Дата постановки");
    private TableColumn<Active, String> clmnFunc = new TableColumn<>("Функция");
    private TableColumn<Active, String> clmnStat = new TableColumn<>("Статус");
    private TableColumn<Active, String> clmnProv = new TableColumn<>("Поставщик");
    private TableColumn<Active, BigDecimal> clmnPrice = new TableColumn<>("Цена");
    private TableColumn<Active, String> clmnCategory = new TableColumn<>("Категория");

    private ObservableList<Active> allActives = FXCollections.observableArrayList();
    private ObservableList<Active> filteredDataMainTable = FXCollections.observableArrayList();
    private Map<Integer,String> mapNameColumn = new HashMap<>();
    //загружаем настройки видимости столбцов для текущего пользователя
    private SettingMainTable settingVisibleClmnMainTable = SettingMainTableModel.getFromId(1L);
    @FXML    private CheckComboBox<String> chkComboBoxClmnVisible;
    @FXML    private CheckTreeView<StateDTO> checkTreeViewStructure;
    @FXML    private CheckComboBox<String> checkBoxCategory;
    @FXML    private Button btnStructure;
    @FXML    private TableView<Active> mainTable;
    @FXML    private TabPane tabPaneOutput;
    @FXML    private Tab tabAll;
    @FXML    private TextField tfFilterStructure;


    public Persona getCurrentPersona() {
        return currentPersona;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createStructure();
        filteredMainTableFromPersona();
        //checkTreeViewStructure.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        checkTreeViewStructure.setContextMenu(new ContextMenu(addFix));

        //создаем таблицу вывода данных
        createMainTable();
        initCheckComboBoxClmnVisible();
        initTextFieldFilterStructure();
        mainTable.setItems(filteredDataMainTable);
        mainTable.getSortOrder().add(clmnMol);
    }
    private void initTextFieldFilterStructure() {
        tfFilterStructure.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<TreeItem<StateDTO>> nodes = checkTreeViewStructure.getRoot().getChildren();
            for (TreeItem<StateDTO> node : nodes) {
                String nodesText = node.getValue().getTitle();
                System.out.println(nodesText);
                if (nodesText.matches(newValue)) {
                    System.out.println(nodesText + "!!!");
                }
            }
        });
    }

    private void initCheckComboBoxClmnVisible() {
        chkComboBoxClmnVisible.getItems().addAll("Актив", "Мол", "Количество", "Номер бух. учета", "Заводской номер",
                "Инвентарный номер", "Дата бух.учет", "Дата ввода в экспл.", "Функция", "Статус", "Поставщик", "Цена", "Категория", "Новый Мол");
        if (settingVisibleClmnMainTable.isClmnTitle())              chkComboBoxClmnVisible.getCheckModel().checkIndices(0);
        if (settingVisibleClmnMainTable.isClmnMol())                chkComboBoxClmnVisible.getCheckModel().checkIndices(1);
        if (settingVisibleClmnMainTable.isClmnAmount())             chkComboBoxClmnVisible.getCheckModel().checkIndices(2);
        if (settingVisibleClmnMainTable.isClmnAccountNumber())      chkComboBoxClmnVisible.getCheckModel().checkIndices(3);
        if (settingVisibleClmnMainTable.isClmnFactoryNumber())      chkComboBoxClmnVisible.getCheckModel().checkIndices(4);
        if (settingVisibleClmnMainTable.isClmnInventoryNumber())    chkComboBoxClmnVisible.getCheckModel().checkIndices(5);
        if (settingVisibleClmnMainTable.isClmnDateAccounting())     chkComboBoxClmnVisible.getCheckModel().checkIndices(6);
        if (settingVisibleClmnMainTable.isClmnDateComissions())     chkComboBoxClmnVisible.getCheckModel().checkIndices(7);
        if (settingVisibleClmnMainTable.isClmnFunctionActive())     chkComboBoxClmnVisible.getCheckModel().checkIndices(8);
        if (settingVisibleClmnMainTable.isClmnStatusActive())       chkComboBoxClmnVisible.getCheckModel().checkIndices(9);
        if (settingVisibleClmnMainTable.isClmnProvider())           chkComboBoxClmnVisible.getCheckModel().checkIndices(10);
        if (settingVisibleClmnMainTable.isClmnPrice())              chkComboBoxClmnVisible.getCheckModel().checkIndices(11);
        if (settingVisibleClmnMainTable.isClmnCategory())           chkComboBoxClmnVisible.getCheckModel().checkIndices(12);
        if (settingVisibleClmnMainTable.isClmnNewMol())             chkComboBoxClmnVisible.getCheckModel().checkIndices(13);
        chkComboBoxClmnVisible.getCheckModel().getCheckedItems().addListener((ListChangeListener<String>) change -> {
            while (change.next()) {
                if (!change.getAddedSubList().isEmpty()) {
                    switch (change.getAddedSubList().get(0).toString()) {
                        case ("Актив")              : clmnActive.setVisible(true);      break;
                        case ("Мол")                : clmnMol.setVisible(true);         break;
                        case ("Новый Мол")          : clmnNewMol.setVisible(true);      break;
                        case ("Количество")         : clmnAmount.setVisible(true);      break;
                        case ("Номер бух. учета")   : clmnAccNumb.setVisible(true);     break;
                        case ("Заводской номер")    : clmnFactNumb.setVisible(true);    break;
                        case ("Инвентарный номер")  : clmnInvNumb.setVisible(true);     break;
                        case ("Дата бух.учет")      : clmnDateAcc.setVisible(true);     break;
                        case ("Дата ввода в экспл."): clmnDateComiss.setVisible(true);  break;
                        case ("Функция")            : clmnFunc.setVisible(true);        break;
                        case ("Статус")             : clmnStat.setVisible(true);        break;
                        case ("Поставщик")          : clmnProv.setVisible(true);        break;
                        case ("Цена")               : clmnPrice.setVisible(true);       break;
                        case ("Категория")          : clmnCategory.setVisible(true);    break;
                    }
                }

                if (!change.getRemoved().isEmpty()) {
                    switch (change.getRemoved().get(0).toString()) {
                        case ("Актив")              : clmnActive.setVisible(false);     break;
                        case ("Мол")                : clmnMol.setVisible(false);        break;
                        case ("Новый Мол")          : clmnNewMol.setVisible(false);      break;
                        case ("Количество")         : clmnAmount.setVisible(false);     break;
                        case ("Номер бух. учета")   : clmnAccNumb.setVisible(false);    break;
                        case ("Заводской номер")    : clmnFactNumb.setVisible(false);   break;
                        case ("Инвентарный номер")  : clmnInvNumb.setVisible(false);    break;
                        case ("Дата бух.учет")      : clmnDateAcc.setVisible(false);    break;
                        case ("Дата ввода в экспл."): clmnDateComiss.setVisible(false); break;
                        case ("Функция")            : clmnFunc.setVisible(false);       break;
                        case ("Статус")             : clmnStat.setVisible(false);       break;
                        case ("Поставщик")          : clmnProv.setVisible(false);       break;
                        case ("Цена")               : clmnPrice.setVisible(false);      break;
                        case ("Категория")          : clmnCategory.setVisible(false);   break;
                    }
                }

            }
        });

    }

    private void createMainTable() {
        mainTable = new TableView<>();
        mainTable.getColumns().addAll(clmnActive, clmnMol, clmnAmount, clmnAccNumb, clmnFactNumb, clmnInvNumb,
                clmnDateComiss, clmnDateAcc, clmnFunc, clmnStat, clmnProv, clmnPrice, clmnCategory, clmnNewMol);
        tabAll.setContent(mainTable);
        loadSettingVisibleColumn();
        initColumnMainTable();
        refreshMainTable();
        //clmnTitle.setCellValueFactory(p -> p.getValue().getAbstractActive().getTitle());
    }

    private void filteredMainTableFromPersona() {
        checkTreeViewStructure.getCheckModel().getCheckedItems().addListener(new ListChangeListener<TreeItem<StateDTO>>() {
            public void onChanged(ListChangeListener.Change<? extends TreeItem<StateDTO>> c) {
                ObservableList<TreeItem<StateDTO>> checkedItems = checkTreeViewStructure.getCheckModel().getCheckedItems();
                filteredDataMainTable.clear();
                for (TreeItem<StateDTO> item : checkedItems) {
                    if (!item.getValue().isType()) {
                        for (Active active : allActives) {
                            if (active.getPersona().getId() == item.getValue().getIdState()) {
                                filteredDataMainTable.add(active);
                            }
                        }
                    }
                }
                if (checkedItems.isEmpty()) filteredDataMainTable.addAll(allActives);
                mainTable.getSortOrder().add(clmnMol);
            }
        });
    }

    public void refreshMainTable() {
        if (!filteredDataMainTable.isEmpty()) filteredDataMainTable.clear();
        allActives.addAll(ActiveModel.getAll());
        filteredDataMainTable.addAll(allActives); // первое заполнение

    }

    private void initColumnMainTable() {
        clmnActive.setCellValueFactory(new PropertyValueFactory<>("abstractActive"));
        clmnMol.setCellValueFactory(new PropertyValueFactory<>("persona"));
        clmnNewMol.setCellValueFactory(new PropertyValueFactory<>("newPersona"));
        clmnAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        clmnAccNumb.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        clmnFactNumb.setCellValueFactory(new PropertyValueFactory<>("factoryNumber"));
        clmnInvNumb.setCellValueFactory(new PropertyValueFactory<>("inventoryNumber"));
        clmnDateAcc.setCellValueFactory(new PropertyValueFactory<>("dateAccounting"));
        clmnDateComiss.setCellValueFactory(new PropertyValueFactory<>("dateComissioning"));
        clmnFunc.setCellValueFactory(new PropertyValueFactory<>("functionActive"));
        clmnStat.setCellValueFactory(new PropertyValueFactory<>("statusActive"));
        clmnProv.setCellValueFactory(new PropertyValueFactory<>("provider"));
        clmnPrice.setCellValueFactory(new PropertyValueFactory<>("price0"));
        clmnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void loadSettingVisibleColumn() {
        clmnActive.setVisible(settingVisibleClmnMainTable.isClmnTitle());
        clmnMol.setVisible(settingVisibleClmnMainTable.isClmnMol());
        clmnNewMol.setVisible(settingVisibleClmnMainTable.isClmnNewMol());
        clmnAmount.setVisible(settingVisibleClmnMainTable.isClmnAmount());
        clmnAccNumb.setVisible(settingVisibleClmnMainTable.isClmnAccountNumber());
        clmnFactNumb.setVisible(settingVisibleClmnMainTable.isClmnFactoryNumber());
        clmnInvNumb.setVisible(settingVisibleClmnMainTable.isClmnInventoryNumber());
        clmnDateAcc.setVisible(settingVisibleClmnMainTable.isClmnDateAccounting());
        clmnDateComiss.setVisible(settingVisibleClmnMainTable.isClmnDateComissions());
        clmnFunc.setVisible(settingVisibleClmnMainTable.isClmnFunctionActive());
        clmnStat.setVisible(settingVisibleClmnMainTable.isClmnStatusActive());
        clmnProv.setVisible(settingVisibleClmnMainTable.isClmnProvider());
        clmnPrice.setVisible(settingVisibleClmnMainTable.isClmnPrice());
        clmnCategory.setVisible(settingVisibleClmnMainTable.isClmnCategory());
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
                ImageView imgIconUser = new ImageView(new Image(getClass().getResourceAsStream("/images/user.png")));
                //imgIconUser.setX(15.0);
                imgIconUser.setFitWidth(14);
                imgIconUser.setFitHeight(14);
                HBox boxIcon = new HBox(imgIconUser);
                boxIcon.setPrefWidth(30);
                boxIcon.setAlignment(Pos.CENTER);
                String fio = persona.getFamily() + " " + persona.getName().charAt(0) + "." + persona.getLastname().charAt(0) + ".";
                StateDTO node = new StateDTO(persona.getPosition() + " " + fio, persona.getId(), false);
                CheckBoxTreeItem<StateDTO> itemPersona = new CheckBoxTreeItem<StateDTO>(node, boxIcon);
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
