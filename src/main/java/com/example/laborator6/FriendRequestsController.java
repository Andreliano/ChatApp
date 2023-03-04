package com.example.laborator6;

import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.FriendRequest;
import com.example.laborator6.domain.FriendShip;
import com.example.laborator6.domain.User;
import com.example.laborator6.service.UserFriendsFromService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestsController {

    private final ObservableList<FriendRequest> friendRequestsModel = FXCollections.observableArrayList();

    private final UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();

    private final FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(userDataBaseRepository);

    private UserFriendsFromService userFriendsFromService;

    @FXML
    private Button acceptButton;

    @FXML
    private Button declineButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<FriendRequest> friendRequestsTable;

    @FXML
    private TableColumn<FriendRequest, String> firstName;

    @FXML
    private TableColumn<FriendRequest, String> lastName;

    @FXML
    private TableColumn<FriendRequest, String> status;

    @FXML
    private TableColumn<FriendRequest, String> dateOfSending;

    private Long idUser;

    private Stage friendRequestsStage;

    private Stage friendsStage;

    @FXML
    public void onAcceptButtonClicked() {
        playAnimationForButton(acceptButton);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
                FriendRequest friendRequest = friendRequestsTable.getSelectionModel().getSelectedItem();
                if (friendRequest != null && friendRequest.getStatus().equals("Pending")) {
                    friendShipDataBaseRepository.updateStatusFromFriendRequests(friendRequest.getIdUser(), idUser, "Accepted");
                    String friendsFrom = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    setFriendRequestsModel();
                    userFriendsFromService.saveFriendInDataBase(idUser, userDataBaseRepository.findOne(friendRequest.getIdUser()), friendsFrom);
                }
        });

        pauseTransition.play();
        //userFriendsFromService.saveFriendInDataBase(friendRequest.getIdUser(), userDataBaseRepository.findOne(idUser), friendsFrom);
    }

    @FXML
    public void onDeclineButtonClicked() {
        playAnimationForButton(declineButton);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
            FriendRequest friendRequest = friendRequestsTable.getSelectionModel().getSelectedItem();
            if (friendRequest != null && friendRequest.getStatus().equals("Pending")) {
                friendShipDataBaseRepository.updateStatusFromFriendRequests(friendRequest.getIdUser(), idUser, "Declined");
                setFriendRequestsModel();
            }
        });

        pauseTransition.play();
    }

    @FXML
    public void onBackButtonClicked() {
        playAnimationForButton(backButton);
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
            friendRequestsStage.close();
            friendsStage.show();
        });
        pauseTransition.play();
    }


    @FXML
    public void initialize() {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateOfSending.setCellValueFactory(new PropertyValueFactory<>("dateOfSending"));
        friendRequestsTable.setItems(friendRequestsModel);
    }

    public void setService(UserFriendsFromService userFriendsFromService) {
        this.userFriendsFromService = userFriendsFromService;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setFriendsStage(Stage friendsStage) {
        this.friendsStage = friendsStage;
    }

    public void setFriendRequestsStage(Stage friendRequestsStage) {
        this.friendRequestsStage = friendRequestsStage;
    }

    public void setFriendRequestsModel() {
        List<FriendShip<Long>> friendShipList = friendShipDataBaseRepository.getUserFriendRequests(idUser);
        List<FriendRequest> friendRequests = new ArrayList<>();


        for (FriendShip<Long> friendShip : friendShipList) {
            User user = userDataBaseRepository.findOne(friendShip.getIdUser1());
            FriendRequest friendRequest = new FriendRequest(user.getId(), user.getFirstName(), user.getLastName(), friendShip.getStatus(), friendShip.getDateOfSending());
            friendRequests.add(friendRequest);
        }

        friendRequestsModel.setAll(friendRequests);

    }

    private void playAnimationForButton(Button button){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }


}
