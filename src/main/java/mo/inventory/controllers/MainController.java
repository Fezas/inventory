/*
 * Copyright (c) 2022
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mo.inventory.dto.StateDTO;
import mo.inventory.entity.Persona;
import mo.inventory.entity.Structure;
import mo.inventory.model.PersonaModel;
import mo.inventory.model.StructureModel;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.CheckTreeView;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private StateDTO node;
    @FXML    private CheckTreeView<StateDTO> checkTreeViewStructure;
    @FXML    private CheckComboBox<String> checkBoxCategory;
    @FXML    private Button btnStructure;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createStructure();
        checkTreeViewStructure.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //checkTreeViewStructure.setShowRoot(false);
        checkTreeViewStructure.setCellFactory(ttc -> new TreeCell<StateDTO>() {
            private final FontIcon graphic = new FontIcon("anto-user");
            @Override
            protected void updateItem(StateDTO item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                    setGraphic(null);
                    return;
                }
                StateDTO node = getItem();
                if (node != null) {
                    setGraphic(node.isType() ? null : graphic);
                }
                setText(empty ? null : String.valueOf(item.getTitle()));
            }
        });
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


}
