package main.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class AlertController {

    public static void showWarningAlert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static Boolean saveProgressAlert(){
        AtomicReference<Boolean> save = new AtomicReference<>();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Window window = alert.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> alert.hide());
        alert.setHeaderText("Do you want to save your progress?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                save.set(true);
            } else if (response == noButton) {
                save.set(false);
            }
        });
        return save.get();
    }

}
