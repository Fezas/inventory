/*
 * Copyright (c) 2023. Stepantsov P.V.
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
import mo.inventory.dto.CategoryDTO;
import mo.inventory.entity.CategoryActive;
import mo.inventory.model.CategoryActiveModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class CategoryActiveController implements Initializable {
    @FXML
    private TreeTableColumn<CategoryDTO, String> clmnAdd, clmnDelete, clmnEdit, clmnTitle;
    @FXML    private TreeTableView<CategoryDTO> tblCategory;
    private final CategoryActiveModel model = new CategoryActiveModel();
    private CategoryDTO node;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //привязка столбцов таблицы к свойствам объекта
        clmnTitle.setCellValueFactory(new TreeItemPropertyValueFactory<CategoryDTO, String>("title"));
        clmnAdd.setCellValueFactory(new TreeItemPropertyValueFactory<CategoryDTO, String>("btnAddNode"));
        clmnDelete.setCellValueFactory(new TreeItemPropertyValueFactory<CategoryDTO, String>("btnDelete"));
        clmnEdit.setCellValueFactory(new TreeItemPropertyValueFactory<CategoryDTO, String>("btnEdit"));
        createStructure();
    }

    public void createStructure() {
        CategoryActive root = model.getRootCategory();
        CategoryDTO rt = createNode(root);
        rt.getBtnDelete().setVisible(false);
        TreeItem<CategoryDTO> itemRoot = new TreeItem<CategoryDTO>(rt); // корень всей структуры
        itemRoot.setExpanded(true);
        structure(itemRoot);
        tblCategory.setRoot(itemRoot);
    }

    /**
     * Функция создания узла штата {@link CategoryActive} в структуре типа {@link TreeTableView}
     * @return возвращает узел {@link CategoryDTO}
     */

    private CategoryDTO createNode(CategoryActive category) {
        node = new CategoryDTO(category.getTitle().toUpperCase(Locale.ROOT), category.getId());
        addAction(node);
        return node;
    }

    public void refresh() {
        tblCategory.getRoot().getChildren().clear();
        structure(tblCategory.getRoot());
    }

    public void structure(TreeItem<CategoryDTO> itemRoot) {
        List<CategoryActive> data = model.getFromIdCategoryActive(itemRoot.getValue().getIdCategory()); //дочерние узлы
        if (!data.isEmpty()) {
            for (CategoryActive category : data) {
                TreeItem<CategoryDTO> item = new TreeItem<CategoryDTO>(createNode(category));
                item.setExpanded(true);
                itemRoot.getChildren().add(item);
                structure(item);
            }
        }
    }

    private void addAction(CategoryDTO result) {
        result.getBtnEdit().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode(result.getTitle(), model.getFromId(result.getIdCategory()), result.getIdCategory());
            }
        });
        result.getBtnAddNode().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                editNode("Добавление нового узла", new CategoryActive(), result.getIdCategory());
            }
        });
        result.getBtnDelete().setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                showAlertWithHeaderText(result.getTitle(), model.getFromId(result.getIdCategory()));
            }
        });
    }

    public void editNode(String title, CategoryActive category, long id) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/structure-edit.fxml"));
            CategoryEditController categoryEditController = new CategoryEditController(category, id);
            fxmlLoader.setController(categoryEditController);
            CategoryEditController c = fxmlLoader.getController();
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

    private void showAlertWithHeaderText(String title, CategoryActive category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление записи");
        alert.setHeaderText("Внимание!");
        alert.setContentText("Вы действительно хотите удалить запись \"" + title + "\" и все дочерние записи?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            model.delete(category);
            refresh();
        }
    }
}

