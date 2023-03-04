package com.example.laborator6;

import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.User;
import com.example.laborator6.service.FriendRequestService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RequestsSentController{

    private final ObservableList<User> usersWhoReceivedRequestModel = FXCollections.observableArrayList();

    private final UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();

    private final FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(userDataBaseRepository);

    private FriendRequestService friendRequestService;

    private Stage usersStage;

    private Stage requestsSentStage;

    private Long idUser;

    @FXML
    private Button backButton;

    @FXML
    private Button deleteRequestButton;

    @FXML
    private Text username;

    @FXML
    private TableView<User> usersWhoReceivedRequestTable;

    @FXML
    private TableColumn<User, String> firstName;

    @FXML
    private TableColumn<User, String> lastName;

    @FXML
    public void initialize() {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        usersWhoReceivedRequestTable.setItems(usersWhoReceivedRequestModel);
    }

    @FXML
    public void onDeleteRequestButtonClicked() {
        playAnimationForButton(deleteRequestButton);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
            User user = usersWhoReceivedRequestTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                friendRequestService.deleteFriendRequest(idUser, user);
                initModel();
            }
        });
        pauseTransition.play();
    }

    @FXML
    public void onBackButtonClicked() {
        playAnimationForButton(backButton);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
            requestsSentStage.close();
            usersStage.show();
        });
        pauseTransition.play();
    }

    public void setService(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
        initModel();
    }


    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setUsersStage(Stage usersStage) {
        this.usersStage = usersStage;
    }

    public void setRequestsSentStage(Stage requestsSentStage) {
        this.requestsSentStage = requestsSentStage;
    }

    private void initModel() {
        User user = userDataBaseRepository.findOne(idUser);
        username.setText(user.getFirstName() + " " + user.getLastName());
        usersWhoReceivedRequestModel.setAll(friendShipDataBaseRepository.getUsersWhoReceivedFriendRequestWithPendingStatus(idUser));
    }

    private void playAnimationForButton(Button button){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

}
