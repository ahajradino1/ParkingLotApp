package ba.unsa.etf.presenters.signup;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.Question;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.validator.routines.EmailValidator;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;

import static ba.unsa.etf.GluonApplication.SIGNUP_SUCCESS_VIEW;

public class SignupPresenter {

    @FXML
    private Button pickQuestion;

    @FXML
    private View signupView;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField passwordConfirm;

//    @FXML
//    private ComboBox<Question> comboBox;

    @FXML
    private TextField answer;

    @FXML
    private Label firstNameValidator, lastNameValidator, emailValidator, usernameValidator, passwordValidator, confpassValidator, answerValidator;

    @FXML
    private Button signupBtn;

    private Question chosenQuestion;

    private final ObservableList<Question> securityQuestions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        signupView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setTitleText("Create account");
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(event -> GluonApplication.getInstance().switchToPreviousView()));
            }
        });
        JsonArray dbQuestions;
        try {
            HttpResponse httpResponse = HttpUtils.GET("questions", false);
            dbQuestions = httpResponse.getMessage();
            convertToObservableList(dbQuestions);
//            comboBox.getSelectionModel().selectedItemProperty()
//                    .addListener((ChangeListener<Question>) (observable, oldValue, newValue) -> chosenQuestion = newValue);
            validateFirstName();
            validateLastName();
            validateEmail();
            validateUsername();
            validatePasswords();
            validateAnswer();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openDialog() {
        Dialog dialog = new Dialog(false);
    //    dialog.setTitleText("Pick security question");
        CharmListView<Question, Integer> charmListView = new CharmListView<>();
        charmListView.setItems(securityQuestions);
        charmListView.setPadding(new Insets(5,5,5,5));
        charmListView.setCellFactory(new Callback<CharmListView<Question, Integer>, CharmListCell<Question>>() {
            @Override
            public CharmListCell<Question> call(CharmListView<Question, Integer> param) {
                return new CharmListCell<Question>() {
                    @Override
                    public void updateItem(Question item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null && !empty) {
                            Text text = new Text(item.toString());
                            text.setFont(Font.font(12));
                            text.setWrappingWidth(180);
                            setGraphic(text);
                        }
                    }
                };
            }
        });
        charmListView.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            chosenQuestion = newValue;
            pickQuestion.setText(newValue.getLabel());
        });
        dialog.setContent(charmListView);
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> {
            dialog.hide();
        });
        dialog.getButtons().add(okButton);
        dialog.showAndWait();
    }

    public void signup() {
        Alert alert;
        if(!isUsernameAvailable(username.getText())) {
            alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Username " + username.getText() + " is taken! Please enter different one.");
            alert.showAndWait();
        } else if(!isEmailAvailable(email.getText())) {
            alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Email " + email.getText() + " is taken! Please enter different one.");
            alert.showAndWait();
        } else {
            HttpResponse httpResponse = null;
            try {
                httpResponse = HttpUtils.POST("api/auth/signup/" + chosenQuestion.getId(),
                        "{\"firstName\":\"" + firstName.getText()
                                + "\",\"lastName\":\"" + lastName.getText()
                                + "\",\"email\":\"" + email.getText()
                                + "\",\"username\":\"" + username.getText()
                                + "\",\"password\":\"" + password.getText()
                                + "\",\"answer\": {\"text\":\"" + answer.getText() + "\"}}", false);
                if (httpResponse.getCode() == 200) {
                    MobileApplication.getInstance().switchView(SIGNUP_SUCCESS_VIEW);
                } else {
                    alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void convertToObservableList (JsonArray dbQuestions) {
        for(int i = 0; i < dbQuestions.size(); i++) {
            JsonObject question = dbQuestions.getJsonObject(i);
            securityQuestions.add(new Question(question.getInt("id"), question.getString("title")));
            //comboBox.setItems(securityQuestions);
        }
    }

    public boolean isEmailAvailable (String email) {
        HttpResponse isEmailAvailable = null;
        try {
            isEmailAvailable = HttpUtils.GET("api/auth/user/checkEmailAvailability?email="+email, false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return isEmailAvailable.getMessage().getJsonObject(0).getBoolean("available");
    }

    public boolean isUsernameAvailable (String username) {
        HttpResponse isUsernameAvailable = null;
        try {
            isUsernameAvailable = HttpUtils.GET("api/auth/user/checkUsernameAvailability?username="+username, false);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return isUsernameAvailable.getMessage().getJsonObject(0).getBoolean("available");
    }

    public void validateFirstName() {
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                firstNameValidator.setText("This field is required!");
                firstNameValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else if (newValue.length() < 2 || newValue.length() > 40) {
                firstNameValidator.setText("First name should contain between 2 and 40 characters.");
                firstNameValidator.setVisible(true);
                signupBtn.setDisable(true);
            }else {
                firstNameValidator.setVisible(false);
                if(chosenQuestion != null && !lastNameValidator.isVisible() && !emailValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
            //todo ovdje mogu provjeriti jos da li sadrzi samo slova
        });
    }

    public void validateLastName() {
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                lastNameValidator.setText("This field is required!");
                lastNameValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else if (newValue.length() < 2 || newValue.length() > 40) {
                lastNameValidator.setText("Last name should contain between 2 and 40 characters.");
                lastNameValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                lastNameValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !emailValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
            //todo ovdje mogu provjeriti jos da li sadrzi samo slova
        });
    }

    public void validateEmail() {
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            EmailValidator validator = EmailValidator.getInstance();
            if (newValue.length() == 0) {
                emailValidator.setText("This field is required!");
                emailValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else if(!validator.isValid(newValue)) {
                emailValidator.setText("Enter valid email address!");
                emailValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                emailValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });

        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !isEmailAvailable(email.getText())) {
                emailValidator.setText("Email address " + email.getText() + " is taken!");
                emailValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                emailValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });
    }

    public void validateUsername() {
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue && !isUsernameAvailable(username.getText())) {
                usernameValidator.setText("Username " + username.getText() + " is taken!");
                usernameValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                usernameValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !emailValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });
    }

    public void validatePasswords() {
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                passwordValidator.setText("Enter password please!");
                passwordValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else if(passwordConfirm.getText().length() != 0 && !newValue.equals(passwordConfirm.getText())) {
                confpassValidator.setText("Passwords don't match!");
                confpassValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                passwordValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !emailValidator.isVisible() && !usernameValidator.isVisible() && !confpassValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });

        passwordConfirm.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(password.getText())) {
                confpassValidator.setText("Passwords don't match!");
                confpassValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                confpassValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !emailValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !answerValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });
    }

    public void validateAnswer() {
        answer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                answerValidator.setText("Enter answer for chosen security question!");
                answerValidator.setVisible(true);
                signupBtn.setDisable(true);
            } else {
                answerValidator.setVisible(false);
                if(chosenQuestion != null && !firstNameValidator.isVisible() && !lastNameValidator.isVisible() && !emailValidator.isVisible() && !usernameValidator.isVisible() && !passwordValidator.isVisible() && !confpassValidator.isVisible())
                    signupBtn.setDisable(false);
            }
        });
    }

}
