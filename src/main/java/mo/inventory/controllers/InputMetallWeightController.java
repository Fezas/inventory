/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mo.inventory.dto.MetallDTO;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class InputMetallWeightController implements Initializable {
    private AbstractEditController controller;
    private TableRow<MetallDTO> row;
    @FXML    private Button btnCancel, btnSave;
    @FXML    private TextField tfWeight;

    public InputMetallWeightController(TableRow<MetallDTO> row) {
        this.row = row;
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void save(ActionEvent event) {
        if (tfWeight.getText().equals("0.0")) {
            controller.currentRow.getItem().setWeight(null);
        } else controller.currentRow.getItem().setWeight(tfWeight.getText());
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void setParent (AbstractEditController controller){
        this.controller = controller;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfWeight.setText(row.getItem().getWeight());
        System.out.println(row.getItem().getWeight());
        Pattern validEditingState = Pattern.compile("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?");
        UnaryOperator<TextFormatter.Change> filter = c -> {
            String text = c.getControlNewText();
            if (validEditingState.matcher(text).matches()) {
                return c ;
            } else {
                return null ;
            }
        };
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public Double fromString(String s) {
                if (s.isEmpty() || "-".equals(s) || ".".equals(s) || "-.".equals(s)) {
                    return 0.0 ;
                } else {
                    return Double.valueOf(s);
                }
            }
            @Override
            public String toString(Double d) {
                return d.toString();
            }
        };
        Double defaultWeight;
        String weight = row.getItem().getWeight();
        if (weight == null) {
            defaultWeight = 0.0;
        } else defaultWeight = Double.parseDouble(row.getItem().getWeight());
        TextFormatter<Double> textFormatter = new TextFormatter<>(converter, defaultWeight, filter);
        tfWeight.setTextFormatter(textFormatter);
    }
}
