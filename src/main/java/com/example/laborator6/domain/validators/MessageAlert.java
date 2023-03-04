package com.example.laborator6.domain.validators;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {

    public static void showErrorMessage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Validation Error");
        message.setContentText(text);
        message.show();
    }

}
