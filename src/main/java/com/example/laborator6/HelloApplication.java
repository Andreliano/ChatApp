package com.example.laborator6;

import com.example.laborator6.chat.Server;
import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.validators.UserValidator;
import com.example.laborator6.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        Thread serverThread = new Thread(new Server());
        serverThread.start();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 810, 550);
        stage.setTitle("LogIn");
        stage.setResizable(false);
        stage.setScene(scene);

        LogInController logInController = fxmlLoader.getController();

        UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();
        UserValidator userValidator = new UserValidator();
        FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(userDataBaseRepository);
        UserService userService = new UserService(userDataBaseRepository, friendShipDataBaseRepository, userValidator);

        logInController.setLogInStage(stage);
        logInController.setUserService(userService);

        stage.show();

    }

    public static void main(String[] args) {launch();}
}