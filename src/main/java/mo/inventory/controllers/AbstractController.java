/*
 * Copyright (c) 2022-2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AbstractController implements Initializable {
    @FXML    private AnchorPane anchorPane;
    TabPane tabPane = new TabPane();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab tab = new Tab();
        tab.setText("11111111");
        tabPane.getTabs().add(tab);
        anchorPane.getChildren().add(tabPane);
        tabPanePosition();
    }

    private void tabPanePosition() {
        anchorPane.setTopAnchor(tabPane, 10.0);
        anchorPane.setBottomAnchor(tabPane, 10.0);
        anchorPane.setLeftAnchor(tabPane, 10.0);
        anchorPane.setRightAnchor(tabPane, 10.0);
    }
}
