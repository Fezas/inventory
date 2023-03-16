/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import mo.inventory.dto.StateDTO;
import mo.inventory.entity.AbstractActive;
import mo.inventory.entity.Persona;
import mo.inventory.entity.Structure;
import mo.inventory.model.PersonaModel;
import mo.inventory.model.StructureModel;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SelectPersonController implements Initializable {
    private AbstractActive active;
    private final StructureModel structureModel = new StructureModel();
    private final PersonaModel personaModel = new PersonaModel();
    private StateDTO node;
    @FXML    private TreeTableColumn<StateDTO, String> clmnTitleStructure;

    @FXML    private TreeTableView<StateDTO> tblTreeViewStructure;

    public SelectPersonController(AbstractActive active) {
        this.active = active;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //привязка столбцов таблицы к свойствам объекта
        clmnTitleStructure.setCellValueFactory(new TreeItemPropertyValueFactory<StateDTO, String>("title"));
        createStructure();
        tblTreeViewStructure.setRowFactory(
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
            final Node nodeImageUser = new ImageView(
                    new Image(getClass().getResourceAsStream("/images/user.png"))
            );
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
                    setGraphic(node.isType() ? null : nodeImageUser);
                }
                setText(empty ? null : item);
            }
        });
    }

    public void createStructure() {
        Structure root = StructureModel.getRootStructure();
        StateDTO rt = createNode(root);
        rt.getBtnDelete().setVisible(false);
        TreeItem<StateDTO> itemRoot = new TreeItem<StateDTO>(rt); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblTreeViewStructure.setRoot(itemRoot);
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
}
