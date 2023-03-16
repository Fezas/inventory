/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.util;

import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import mo.inventory.dto.StateDTO;

public class DefaultListCell<T> extends TreeCell<T> {
    final Node nodeImageUser = new ImageView(
            new Image(getClass().getResourceAsStream("/images/user.png"))
    );
    @Override public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (item instanceof Node) {
            setText(null);
            Node currentNode = getGraphic();
            Node newNode = (Node) item;
            if (currentNode == null || ! currentNode.equals(newNode)) {
                setGraphic(newNode);
            }
        } else if (item instanceof StateDTO) { // подразумевался полностью абстрактный класс, но  пришлось привязаться к реализации
            if (!((StateDTO) item).isType()) {
                setGraphic(nodeImageUser);
                setText(((StateDTO) item).getTitle());
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && (!isEmpty())) {
                        T rowData = getItem();
                        System.out.println(rowData.toString());
                    }
                });
            } else {
                setText(((StateDTO) item).getTitle());
                setGraphic(null);
            }
        } else {
            setText(item == null ? "null" : item.toString());
            setGraphic(null);
        }
    }
}