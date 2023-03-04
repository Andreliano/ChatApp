package com.example.laborator6.chat;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.w3c.dom.ls.LSOutput;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.sql.SQLException;

public class Client implements Runnable {

    private final VBox boxOfMessages;

    private final Long idUser;

    public Client(VBox boxOfMessages, Long idUser) {
        this.boxOfMessages = boxOfMessages;
        this.idUser = idUser;
    }

    @Override
    public void run() {

        try (ServerSocket client = new ServerSocket((int) (idUser + 5000))) {

            while (true) {

                Socket c = client.accept();

                System.out.println("Client accepted message");

                InputStream inputStream = c.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);

                String message = dataInputStream.readUTF();

                System.out.println(message);

                //if (!sameConnection) {
                    TextField mess = new TextField();
                    mess.setEditable(false);
                    mess.setBackground(new Background(new BackgroundFill(Color.rgb(216, 110, 57), CornerRadii.EMPTY, Insets.EMPTY)));
                    mess.setText(message);
                    mess.setAlignment(Pos.CENTER);
                    mess.setTranslateX(810 - message.length() * 36);
                    mess.setMaxWidth(message.length() * 36);
                    mess.setStyle("-fx-shape: \"M0 20 Q0 0 20 0 L180 0 Q200 0 200 20 L200 80 Q200 100 180 100 L20 100 Q0 100 0 80 Z\"");

                    Platform.runLater(() -> boxOfMessages.getChildren().add(mess));
                //}
            }
        }catch (IOException ignored) {

            }
    }

}
