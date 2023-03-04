package com.example.laborator6;
import com.example.laborator6.database.ConnectionInfoDataBaseRepository;
import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.domain.UserFriendsFrom;
import com.example.laborator6.service.UserFriendsFromService;
import com.example.laborator6.service.UserService;
import com.example.laborator6.utils.PasswordEncryption;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    private UserService userService;

    private Stage logInStage;

    private final ConnectionInfoDataBaseRepository connectionInfoDataBaseRepository = new ConnectionInfoDataBaseRepository();


    @FXML
    private Button logInButton;

    @FXML
    private Button signUpButton;

    @FXML
    private GridPane logInformation;

    @FXML
    private Text textResponse;
    @FXML
    public void userLogIn(){

        playAnimationForButton(logInButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        TextField email = (TextField) getNodeFromGridPane(logInformation, 1, 0);
        TextField password = (TextField) getNodeFromGridPane(logInformation, 1, 1);

        pauseTransition.setOnFinished(e -> {

            String passwordEncrypted = null;
            try {
                passwordEncrypted = PasswordEncryption.toHexString(PasswordEncryption.getSHA(password.getText()));
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }

            if (userService.checkUserAccountExistence(email.getText(), passwordEncrypted)) {
                //textResponse.setText("Authentication succeed");
                User user = userService.findUserByEmail(email.getText());

                InetAddress IP = null;
                try {
                    IP = InetAddress.getByName(InetAddress.getLocalHost().getHostAddress());
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }
                String dateOfConnection = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                connectionInfoDataBaseRepository.saveConnectionInformation(user.getId(), String.valueOf(IP), dateOfConnection, null);
                email.clear();
                password.clear();


                logInStage.close();

                Stage friendsStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("friends-view.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 766, 680);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                friendsStage.setTitle("UserFriends");
                friendsStage.setResizable(false);
                friendsStage.setScene(scene);

                FriendsController friendsController = fxmlLoader.getController();

                UserFriendsFromService userFriendsFromService = new UserFriendsFromService();
                userFriendsFromService.setUserFriendsFromList(buildUserFriendsList(user));

                friendsController.setIdUser(user.getId());
                friendsController.setService(userFriendsFromService);
                friendsController.setLogInStage(logInStage);
                friendsController.setFriendsStage(friendsStage);

                friendsStage.show();
            } else {
                textResponse.setText("Incorrect email address or password");
            }
        });

        pauseTransition.play();

    }

    @FXML
    public void newSignUp() {


        playAnimationForButton(signUpButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signUp-view.fxml"));

            Stage signUpStage = new Stage();
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 810, 550);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            signUpStage.setTitle("SignUp");
            signUpStage.setResizable(false);
            signUpStage.setScene(scene);
            signUpStage.show();

            SignUpController signUpController = fxmlLoader.getController();
            signUpController.setStage(signUpStage);
            signUpController.setLogInStage(logInStage);
            signUpController.setTextResponseVisibleToFalse();
            signUpController.setService(userService);

            this.logInStage.close();
        });

        pauseTransition.play();
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public void setLogInStage(Stage logInStage) {
        this.logInStage = logInStage;
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        return getNode(gridPane, col, row);
    }

    public static Node getNode(GridPane gridPane, int col, int row) {
        ObservableList<Node> children = gridPane.getChildren();
        for (Node node : children) {
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            if (columnIndex == null)
                columnIndex = 0;
            if (rowIndex == null)
                rowIndex = 0;

            if (columnIndex == col && rowIndex == row) {
                return node;
            }
        }
        return null;
    }

    private List<UserFriendsFrom> buildUserFriendsList(User user) {

        List<UserFriendsFrom> usersList = new ArrayList<>();
        FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(new UserDataBaseRepository());
        List<FriendShip<Long>> friendShipList = friendShipDataBaseRepository.getUserFriends(user.getId());

        for (FriendShip<Long> friendShip : friendShipList) {
            User u = userService.findOne(friendShip.getIdUser2());
            UserFriendsFrom userFriendsFrom = new UserFriendsFrom(u, friendShip.getFriendsFrom());
            usersList.add(userFriendsFrom);
        }

        return usersList;

    }

    private void playAnimationForButton(Button button){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }


}
