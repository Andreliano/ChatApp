package com.example.laborator6;
import com.example.laborator6.domain.User;
import com.example.laborator6.service.FriendRequestService;
import com.example.laborator6.utils.events.FriendRequestChangeEvent;
import com.example.laborator6.utils.observer.Observer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UsersController implements Observer<FriendRequestChangeEvent> {

    private final ObservableList<User> usersModel = FXCollections.observableArrayList();
    private FriendRequestService friendRequestService;
    private Long idUser;

    private Stage usersStage;

    private Stage friendsStage;

    @FXML
    private Button sendRequestButton;

    @FXML
    private Button requestsSentButton;

    @FXML
    private Button backButton;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView<User> usersListView;

    @FXML
    private Text sendRequestMessage;


    @FXML
    public void initialize(){
        searchBar.textProperty().addListener(f -> userFilterByFirstName());
        usersListView.setItems(usersModel);
        usersListView.setCellFactory(list -> new ListCell<>(){
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getFirstName() + " " + item.getLastName());
                }
            }
        });
    }

    @FXML
    public void onSendRequestButtonClicked(){

        playAnimationForButton(sendRequestButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            User user = usersListView.getSelectionModel().getSelectedItem();

            if (user == null) {
                sendRequestMessage.setText("Friend request couldn't be sent");
            } else {
                sendRequestMessage.setText(String.format("Friend request to \"%s %s\" sent successfully", user.getFirstName(), user.getLastName()));
                friendRequestService.saveFriendRequest(idUser, user);
            }
        });
        pauseTransition.play();
    }

    @FXML
    public void onRequestsSentButtonClicked() {

        playAnimationForButton(requestsSentButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            usersStage.close();

            Stage requestsSentStage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("requestsSent-view.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 810, 560);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            requestsSentStage.setScene(scene);
            requestsSentStage.setResizable(false);

            RequestsSentController requestsSentController = fxmlLoader.getController();
            requestsSentController.setIdUser(idUser);
            requestsSentController.setUsersStage(usersStage);
            requestsSentController.setRequestsSentStage(requestsSentStage);
            requestsSentController.setService(friendRequestService);

            requestsSentStage.show();
        });

        pauseTransition.play();

    }

    @FXML
    public void onBackButtonClicked(){
        playAnimationForButton(backButton);
        PauseTransition pauseTransition = new PauseTransition();
        pauseTransition.setOnFinished(e -> {
            usersStage.close();
            friendsStage.show();
        });
        pauseTransition.play();
    }

    public void userFilterByFirstName(){
        String firstName = searchBar.getText();
        Predicate<User> userPredicateName;

        if(firstName.equals("")){
            userPredicateName = user -> false;
        }
        else {
            userPredicateName = user -> user.getFirstName().contains(firstName);
        }

        List<User> usersFiltered = friendRequestService.getListOfUsersWithoutFriendRequest().stream().filter(userPredicateName).collect(Collectors.toList());
        usersModel.setAll(usersFiltered);
    }

    public void setService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
        friendRequestService.addObserver(this);
        userFilterByFirstName();
    }


    public void setIdUser(Long idUser){
        this.idUser = idUser;
    }

    public void setUsersStage(Stage usersStage){
        this.usersStage = usersStage;
    }

    public void setFriendsStage(Stage friendsStage){
        this.friendsStage = friendsStage;
    }


    @Override
    public void update(FriendRequestChangeEvent friendRequestChangeEvent) {
        userFilterByFirstName();
    }

    private void playAnimationForButton(Button button){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

}
