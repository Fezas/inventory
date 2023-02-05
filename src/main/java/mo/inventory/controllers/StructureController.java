/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mo.inventory.dto.StateDTO;
import mo.inventory.entity.Persona;
import mo.inventory.entity.Structure;
import mo.inventory.model.PersonaModel;
import mo.inventory.model.StructureModel;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class StructureController implements Initializable {
    @FXML    private TreeTableColumn<StateDTO, String> clmnAddStructure, clmnDeleteStructure, clmnEditStructure, clmnAddPersona;
    @FXML    private TreeTableColumn<StateDTO, String> clmnTitleStructure;
    @FXML    private TreeTableView<StateDTO> tblStructure;
    private final StructureModel structureModel = new StructureModel();
    private final PersonaModel personaModel = new PersonaModel();
    private StateDTO node;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("title"));
        clmnAddStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("btnAddNode"));
        clmnDeleteStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("btnDelete"));
        clmnEditStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("btnEdit"));
        clmnAddPersona.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("btnAddPersona"));
        createStructure();

        tblStructure.setRowFactory(
                new Callback<TreeTableView<StateDTO>, TreeTableRow<StateDTO>>() {
                    @Override
                    public TreeTableRow<StateDTO> call(TreeTableView<StateDTO> tableView) {
                        final TreeTableRow<StateDTO> row = new TreeTableRow<StateDTO>();
                        row.setOnMouseEntered(event -> {
                            if (row.getTreeItem() != null) {
                                if (row.getTreeItem().getValue().isType()) {
                                    row.getTreeItem().getValue().getBtnAddNode().setVisible(true);
                                    row.getTreeItem().getValue().getBtnAddPersona().setVisible(true);
                                }
                                row.getTreeItem().getValue().getBtnEdit().setVisible(true);
                                if (row.getTreeItem().getParent() != null) {
                                    row.getTreeItem().getValue().getBtnDelete().setVisible(true);
                                } else row.getTreeItem().getValue().getBtnDelete().setVisible(false);
                            }
                        });
                        row.setOnMouseExited(event -> {
                            if (row.getTreeItem() != null) {
                                row.getTreeItem().getValue().getBtnAddNode().setVisible(false);
                                row.getTreeItem().getValue().getBtnAddPersona().setVisible(false);
                                row.getTreeItem().getValue().getBtnEdit().setVisible(false);
                                row.getTreeItem().getValue().getBtnDelete().setVisible(false);
                            }
                        });
                        return row;
                    }
                }
        );
        //тут была какая то дикая ошибка - при коллапсе слетают иконки молов, но после оптимизации кода куда то делась....
        clmnTitleStructure.setCellFactory(ttc -> new TreeTableCell<StateDTO, String>() {
            private final FontIcon graphic = new FontIcon("anto-user");
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setText(null);
                    setGraphic(null);
                    return;
                }
                StateDTO node = getTableRow().getItem();
                if (node != null) {
                    setGraphic(node.isType() ? null : graphic);
                }
                setText(empty ? null : item);
            }
        });
    }

    public void refresh() {
        tblStructure.getRoot().getChildren().clear();
        structure(tblStructure.getRoot());
    }

    public void createStructure() {
        Structure root = StructureModel.getRootStructure();
        StateDTO rt = createNode(root);
        rt.getBtnDelete().setVisible(false);
        TreeItem<StateDTO> itemRoot = new TreeItem<StateDTO>(rt); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblStructure.setRoot(itemRoot);
    }
    
    /**
     * Функция создания узла штата {@link Structure} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link StateDTO}
     */
    
    private StateDTO createNode(Structure structure) {
        node = new StateDTO(structure.getTitle().toUpperCase(Locale.ROOT), structure.getId(), true);
        if (structure.getAddress() != null) {
            if (!structure.getAddress().isEmpty()) node.setTitle(node.getTitle() + " | Адрес: " + structure.getAddress());
        }
        if (structure.getInn() != null) {
            if (!structure.getInn().isEmpty())  node.setTitle(node.getTitle() + " | ИНН: " + structure.getInn());
        }
        addAction(node);
        return node;
    }

    /**
     * Функция рекурсивного формирования {@link TreeTableView} с помощью запроса  {@link StructureModel#getFromIdStructure(long)}
     */
    
    public void structure(TreeItem<StateDTO> itemRoot) {
        List<Structure> data = structureModel.getFromIdStructure(itemRoot.getValue().getIdState()); //дочерние узлы
        List<Persona> persons = personaModel.getFromIdStructure(itemRoot.getValue().getIdState()); //персоны в узлах
        if (!persons.isEmpty()) {
            for (Persona persona : persons) {
                String fio = persona.getFamily() + " " + persona.getName().charAt(0) + "." + persona.getLastname().charAt(0) + ".";
                StateDTO node = new StateDTO(persona.getPosition() + " " + fio, persona.getId(), false);
                addAction(node);
                TreeItem<StateDTO> itemPersona = new TreeItem<StateDTO>(node);
                itemPersona.getValue().getBtnAddNode().setVisible(false);
                itemPersona.getValue().getBtnAddPersona().setVisible(false);
                itemRoot.getChildren().add(itemPersona);
            }
        }
        if (!data.isEmpty()) {
            for (Structure structure : data) {
                TreeItem<StateDTO> item = new TreeItem<StateDTO>(createNode(structure));
                item.setExpanded(true);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    private void addAction(StateDTO result) {
        if (result.isType()) {
            result.getBtnEdit().setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    editNode(result.getTitle(), StructureModel.getFromId(result.getIdState()), result.getIdState());
                }
            });
            result.getBtnAddNode().setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    editNode("Добавление нового узла", new Structure(), result.getIdState());
                }
            });
            result.getBtnAddPersona().setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    editPersona("Добавление нового служащего", new Persona(), result.getIdState());
                }
            });
        } else {
            result.getBtnEdit().setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    editPersona(result.getTitle(), PersonaModel.getFromId(result.getIdState()), result.getIdState());
                }
            });
        }
        result.getBtnDelete().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertWithHeaderText(result.getTitle(), StructureModel.getFromId(result.getIdState()));
            }
        });
    }

    public void editNode(String title, Structure structure, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/structure-edit.fxml"));
            StructureEditController structureEditController = new StructureEditController(structure, id);
            fxmlLoader.setController(structureEditController);
            StructureEditController c = fxmlLoader.getController();
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

    public void editPersona(String title, Persona persona, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/persona-edit.fxml"));
            PersonEditController personEditController = new PersonEditController(persona, id);
            fxmlLoader.setController(personEditController);
            PersonEditController c = fxmlLoader.getController();
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

    private void showAlertWithHeaderText(String title, Structure structure) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все дочерние записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            structureModel.delete(structure);
            refresh();
        }
    }
}
