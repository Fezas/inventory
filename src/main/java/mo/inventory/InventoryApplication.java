/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mo.inventory.util.HibernateUtil;

import java.io.IOException;

public class InventoryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("/views/main.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(InventoryApplication.class.getResource("/views/persons.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        //scene.getStylesheets().add(getClass().getResource("/css/structure-table.css").toExternalForm());
        stage.setTitle("Спецдок БД");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        HibernateUtil.getSessionFactory().openSession();
        launch();
    }
}