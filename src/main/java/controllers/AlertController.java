package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Window;

import java.util.concurrent.atomic.AtomicReference;

public class AlertController {

    public static void showWarningAlert(String headerText, String contextText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static void showInfoAlert(String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.showAndWait();
    }

    public static void showErrorAlert(String headerText, String contextText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
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
