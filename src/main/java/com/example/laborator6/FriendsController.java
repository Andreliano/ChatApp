package com.example.laborator6;

import com.example.laborator6.database.ConnectionInfoDataBaseRepository;
import com.example.laborator6.database.FriendShipDataBaseRepository;
import com.example.laborator6.database.UserDataBaseRepository;
import com.example.laborator6.domain.User;
import com.example.laborator6.domain.UserFriendsFrom;
import com.example.laborator6.service.FriendRequestService;
import com.example.laborator6.service.UserFriendsFromService;
import com.example.laborator6.utils.events.UserFriendsFromEntityChangeEvent;
import com.example.laborator6.utils.observer.Observer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer<UserFriendsFromEntityChangeEvent>{


    private final ObservableList<UserFriendsFrom> userFriendsFromModel = FXCollections.observableArrayList();

    private final UserDataBaseRepository userDataBaseRepository = new UserDataBaseRepository();

    private final ConnectionInfoDataBaseRepository connectionInfoDataBaseRepository = new ConnectionInfoDataBaseRepository();

    private final FriendShipDataBaseRepository friendShipDataBaseRepository = new FriendShipDataBaseRepository(userDataBaseRepository);

    private UserFriendsFromService userFriendsFromService;

    @FXML
    private Button backButton;

    @FXML
    private Button addFriendButton;

    @FXML
    private Button deleteFriendButton;

    @FXML
    private Button friendRequestsButton;

    @FXML
    private TableView<UserFriendsFrom> friendsTable;

    @FXML
    private TableColumn<User, String> firstName;

    @FXML
    private TableColumn<User, String> lastName;

    @FXML
    private TableColumn<User, String> friendsFrom;

    @FXML
    private Text username;

    @FXML
    private ImageView profilePhoto;

    private Stage logInStage;

    private Stage friendsStage;

    private Long idUser;

    @FXML
    public void initialize() {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        friendsFrom.setCellValueFactory(new PropertyValueFactory<>("friendsFrom"));
        friendsTable.setItems(userFriendsFromModel);
    }

    @FXML
    public void onProfilePhotoClicked(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            profilePhoto.setImage(image);
        }
    }

    @FXML
    public void onDoubleClick(){
        friendsTable.setOnMouseClicked(event -> {
            UserFriendsFrom userFriendsFrom = friendsTable.getSelectionModel().getSelectedItem();
            if(userFriendsFrom != null) {
                User user = userFriendsFrom.getUser();
                if (event.getClickCount() == 2 && user != null) {
                    this.friendsStage.close();

                    Stage chatStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-view.fxml"));

                    Scene scene;
                    try {
                        scene = new Scene(fxmlLoader.load(), 810, 550);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    chatStage.setTitle("Chat");
                    chatStage.setResizable(false);
                    chatStage.setScene(scene);

                    ChatController chatController = fxmlLoader.getController();
                    chatController.setIdFriend(user.getId());
                    chatController.setIdUser(idUser);
                    chatController.setFriendUsername();
                    chatController.setFriendsStage(friendsStage);
                    chatController.setChatStage(chatStage);
                    chatController.getBoxOfMessages();

                    chatStage.show();


                }
            }
        });
    }

    @FXML
    public void onBackButtonClicked() {


        playAnimationForButton(backButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            String dateOfSignOut = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            connectionInfoDataBaseRepository.updateDateOfSignOut(idUser, dateOfSignOut);
            this.friendsStage.close();
            this.logInStage.show();
        });

        pauseTransition.play();

    }

    @FXML
    public void onDeleteFriendButtonClicked() {

        playAnimationForButton(deleteFriendButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            UserFriendsFrom user = friendsTable.getSelectionModel().getSelectedItem();
            if (user != null) {
                userFriendsFromService.deleteFriendFromDataBase(idUser, user.getUser());
            }
        });

        pauseTransition.play();

    }

    @FXML
    public void onAddFriendButtonClicked() throws IOException {

        playAnimationForButton(addFriendButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            this.friendsStage.close();

            Stage usersStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("users-view.fxml"));


            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 810, 550);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            usersStage.setTitle("AddNewFriendWindow");
            usersStage.setResizable(false);
            usersStage.setScene(scene);

            UsersController usersController = fxmlLoader.getController();

            FriendRequestService friendRequestService = new FriendRequestService();

            friendRequestService.setListOfUsersWithoutFriendRequest(getListOfUsersWithoutFriendRequest());


            usersController.setService(friendRequestService);
            usersController.setIdUser(idUser);
            usersController.setUsersStage(usersStage);
            usersController.setFriendsStage(friendsStage);

            usersStage.show();
        });

        pauseTransition.play();

    }

    @FXML
    public void onFriendRequestsButtonClicked() throws IOException {

        playAnimationForButton(friendRequestsButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));

        pauseTransition.setOnFinished(e -> {

            this.friendsStage.close();

            Stage friendRequestStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("friendRequests-view.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 810, 550);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            friendRequestStage.setTitle("FriendRequests");
            friendRequestStage.setResizable(false);
            friendRequestStage.setScene(scene);

            FriendRequestsController friendRequestsController = fxmlLoader.getController();
            friendRequestsController.setIdUser(idUser);
            friendRequestsController.setFriendRequestsStage(friendRequestStage);
            friendRequestsController.setFriendsStage(friendsStage);
            friendRequestsController.setFriendRequestsModel();
            friendRequestsController.setService(userFriendsFromService);

            friendRequestStage.show();
        });

        pauseTransition.play();

    }

    public List<User> getListOfUsersWithoutFriendRequest() {
        List<User> users = StreamSupport.stream(userDataBaseRepository.findAll().spliterator(), false).collect(Collectors.toList());

        List<User> usersWhoReceivedFriendRequest = friendShipDataBaseRepository.getUsersWhoReceivedAndSentFriendRequest(idUser);

        for (User user : usersWhoReceivedFriendRequest) {
            users.remove(user);
        }

        users.remove(userDataBaseRepository.findOne(idUser));

        return users;
    }


    public void setService(UserFriendsFromService userFriendsFromService) {
        this.userFriendsFromService = userFriendsFromService;
        userFriendsFromService.addObserver(this);
        initModel();
    }

    public void setLogInStage(Stage logInStage) {
        this.logInStage = logInStage;
    }

    public void setFriendsStage(Stage friendsStage) {
        this.friendsStage = friendsStage;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public void update(UserFriendsFromEntityChangeEvent userFriendsFromEntityChangeEvent) {
        initModel();
    }

    private void initModel() {
        User user = userDataBaseRepository.findOne(idUser);
        username.setText(user.getFirstName() + " " + user.getLastName());
        userFriendsFromModel.setAll(userFriendsFromService.getUserFriendsFromList());
    }

    private void playAnimationForButton(Button button){
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

}
