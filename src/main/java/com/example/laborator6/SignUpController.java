package com.example.laborator6;

import com.example.laborator6.domain.User;
import com.example.laborator6.domain.validators.MessageAlert;
import com.example.laborator6.domain.validators.ValidationException;
import com.example.laborator6.service.UserService;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static com.example.laborator6.LogInController.getNode;

public class SignUpController {
    private UserService userService;

    @FXML
    private Button createUserButton;

    @FXML
    private Button backButton;

    @FXML
    private GridPane signUpInformation;

    @FXML
    private Text textResponse;

    private Stage signUpStage;

    private Stage logInStage;

    public void setStage(Stage stage) {
        signUpStage = stage;
    }

    public void setLogInStage(Stage stage) {
        logInStage = stage;
    }

    @FXML
    public void onCreateUserButtonClicked() {

        playAnimationForButton(createUserButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));

        pauseTransition.setOnFinished(e -> {

            TextField email = (TextField) getNodeFromGridPane(signUpInformation, 1, 2);

            if (!userService.checkEmailExistenceInUsers(email.getText())) {
                TextField firstName = (TextField) getNodeFromGridPane(signUpInformation, 1, 0);
                TextField lastName = (TextField) getNodeFromGridPane(signUpInformation, 1, 1);
                TextField password = (TextField) getNodeFromGridPane(signUpInformation, 1, 3);

                User user = new User(firstName.getText(), lastName.getText(), email.getText(), password.getText());

                try {
                    userService.save(user);
                    signUpStage.close();
                    logInStage.show();
                    textResponse.setVisible(true);
                    textResponse.setText("Account created successfully");
                } catch (ValidationException ve) {
                    MessageAlert.showErrorMessage(signUpStage, ve.getMessage());
                }

            } else {
                textResponse.setVisible(true);
                textResponse.setText("The email address corresponds to another account");
            }
        });

        pauseTransition.play();

    }

    @FXML
    public void onBackButtonClicked() {

        playAnimationForButton(backButton);

        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
        pauseTransition.setOnFinished(e -> {
            signUpStage.close();
            logInStage.show();
        });
        pauseTransition.play();
    }

    public void setService(UserService userService) {
        this.userService = userService;
    }

    public void setTextResponseVisibleToFalse() {
        textResponse.setVisible(false);
    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        return getNode(gridPane, col, row);
    }

    private void playAnimationForButton(Button button) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), button);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

}
