package ba.unsa.etf.presenters.change_password;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.io.IOException;

import static ba.unsa.etf.GluonApplication.CHANGE_PASSWORD_SUCCESS_VIEW;

public class ChangePasswordPresenter {
    @FXML
    private View changePassView;

    @FXML
    private PasswordField oldPassword, newPassword, newPasswordConfirm;

    @FXML
    private Label confirmPasswordValidator;

    @FXML
    private Label passwordValidator;

    @FXML
    private Label securityQuestion;

    @FXML
    private TextField answer;

    @FXML
    private Label answerValidator;

    @FXML
    private Button changePassBtn;

    public void initialize() {
        changePassView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Change password");
            }
        });
        getSecurityQuestion();
        validateAnswer();
        validateConfirmPassword();
    }

    public void changePassword() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/change/newPassword",
                    "{\"oldPassword\":\"" + oldPassword.getText()
                            + "\",\"newPassword\":\"" + newPassword.getText()
                            + "\",\"answer\":\"" + answer.getText()
                            + "\"}", true);
            if (httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                MobileApplication.getInstance().switchView(CHANGE_PASSWORD_SUCCESS_VIEW);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void getSecurityQuestion() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/change/securityquestion", "{}", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                System.out.println(httpResponse.getMessage());
               securityQuestion.setText("Answer security question: " + httpResponse.getMessage().getJsonObject(0).getString("title"));
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void validateConfirmPassword() {
        newPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                newPassword.setText("Enter password please!");
                passwordValidator.setVisible(true);
                changePassBtn.setDisable(true);
            } else if(newPasswordConfirm.getText().length() != 0 && !newValue.equals(newPasswordConfirm.getText())) {
                confirmPasswordValidator.setText("Passwords don't match!");
                confirmPasswordValidator.setVisible(true);
                changePassBtn.setDisable(true);
            } else {
                passwordValidator.setVisible(false);
                if(!confirmPasswordValidator.isVisible() && !answerValidator.isVisible())
                    changePassBtn.setDisable(false);
            }
        });

        newPasswordConfirm.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals(newPassword.getText())) {
                confirmPasswordValidator.setText("Passwords don't match!");
                confirmPasswordValidator.setVisible(true);
                changePassBtn.setDisable(true);
            } else {
                confirmPasswordValidator.setVisible(false);
                if(!passwordValidator.isVisible() && !answerValidator.isVisible())
                    changePassBtn.setDisable(false);
            }
        });
    }

    public void validateAnswer() {
        answer.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                answerValidator.setText("Enter answer for chosen security question!");
                answerValidator.setVisible(true);
                changePassBtn.setDisable(true);
            } else {
                answerValidator.setVisible(false);
                if(!passwordValidator.isVisible() && !confirmPasswordValidator.isVisible())
                    changePassBtn.setDisable(false);
            }
        });
    }
}
