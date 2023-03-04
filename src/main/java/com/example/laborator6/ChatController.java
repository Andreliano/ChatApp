package com.example.laborator6;


import com.example.laborator6.chat.Client;
import com.example.laborator6.database.MessageDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.FontCssMetaData;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

public class ChatController {
    private final UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();

    private final MessageDataBaseRepository messageDataBaseRepository = new MessageDataBaseRepository();

    private Long idFriend;

    private Long idUser;

    private Stage friendsStage;

    private Stage chatStage;
    @FXML
    private VBox boxOfMessages;

    @FXML
    private TextField messageBar;

    @FXML
    private VBox texts;

    @FXML
    public void initialize() {

        TextFormatter<String> textFormatter = new TextFormatter<>(change ->
          change.getControlNewText().length() <= 100 ? change : null);
        messageBar.setTextFormatter(textFormatter);

        messageBar.setOnMouseClicked(event -> {


            if (event.getX() >= messageBar.getWidth() - 40 && event.getX() <= messageBar.getWidth() && event.getY() >= messageBar.getHeight() - 40 && event.getY() <= messageBar.getHeight()) {
                if(messageBar.getText().length() > 0) {
                    try {
                        sendMessageToServer();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });


    }

    public void getBoxOfMessages() {
        List<Message> messages = messageDataBaseRepository.getMessages();


        for (Message mess : messages) {
            //System.out.println(mess);

            TextField message = new TextField();

            if (mess.getSentBy().equals(idUser) && mess.getMessage().length() > 0) {
                message.setEditable(false);
                message.setBackground(new Background(new BackgroundFill(Color.rgb(228,135,22), CornerRadii.EMPTY, Insets.EMPTY)));
                message.setText(mess.getMessage());
                message.setAlignment(Pos.CENTER);
                message.setMaxWidth(mess.getMessage().length() * 36);
                message.setStyle("-fx-shape: \"M0 20 Q0 0 20 0 L180 0 Q200 0 200 20 L200 80 Q200 100 180 100 L20 100 Q0 100 0 80 Z\"");
                boxOfMessages.getChildren().add(message);

            } else if (mess.getSentTo().equals(idUser) && mess.getMessage().length() > 0) {
                message.setEditable(false);
                message.setBackground(new Background(new BackgroundFill(Color.rgb(216, 110, 57), CornerRadii.EMPTY, Insets.EMPTY)));
                message.setText(mess.getMessage());
                message.setAlignment(Pos.CENTER);
                message.setMaxWidth(mess.getMessage().length() * 36);

                if(810 - message.getMaxWidth() > 0){
                    message.setTranslateX(810 - message.getMaxWidth());
                }else {
                    message.setTranslateX(200);
                }
                message.setStyle("-fx-shape: \"M0 20 Q0 0 20 0 L180 0 Q200 0 200 20 L200 80 Q200 100 180 100 L20 100 Q0 100 0 80 Z\"");
                boxOfMessages.getChildren().add(message);
                }

            }
    }

    public void setFriendUsername() {

        String username = userDataBaseRepository.findOne(idFriend).getFirstName() + " " + userDataBaseRepository.findOne(idFriend).getLastName();

        Node friendUsername = texts.getChildren().get(0);

        if (friendUsername instanceof Text) {
            ((Text) friendUsername).setText(username);
        }
    }

    @FXML
    public void onExitButtonClicked() {
        chatStage.close();
        friendsStage.show();
    }

    @FXML
    public void getMessage() throws IOException {
        if(messageBar.getText().length() > 0) {
            sendMessageToServer();
        }
    }

    private void sendMessageToServer() throws IOException {
        TextField message = new TextField();

        message.setBackground(new Background(new BackgroundFill(Color.rgb(228,135,22), CornerRadii.EMPTY, Insets.EMPTY)));
        message.setText(messageBar.getText());
        message.setAlignment(Pos.CENTER);



        message.setMaxWidth(messageBar.getText().length() * 35);
        message.setStyle("-fx-shape: \"M0 20 Q0 0 20 0 L180 0 Q200 0 200 20 L200 80 Q200 100 180 100 L20 100 Q0 100 0 80 Z\"");

        boxOfMessages.getChildren().add(message);

        InetAddress IP = InetAddress.getByName("192.168.1.4");
        try(Socket client = new Socket()){

            client.connect(new InetSocketAddress(IP, 1234));

            OutputStream outputStream = client.getOutputStream();

            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF(messageBar.getText());
            dataOutputStream.writeLong(idFriend);
            dataOutputStream.writeLong(idUser);
        }

        messageBar.setText("");
    }

    public void setIdFriend(Long idFriend) {
        this.idFriend = idFriend;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
        Client client = new Client(boxOfMessages, this.idUser);
        Thread thread1 = new Thread(client);
        thread1.start();
    }

    public void setFriendsStage(Stage friendsStage) {
        this.friendsStage = friendsStage;
    }

    public void setChatStage(Stage chatStage) {
        this.chatStage = chatStage;
    }

}
