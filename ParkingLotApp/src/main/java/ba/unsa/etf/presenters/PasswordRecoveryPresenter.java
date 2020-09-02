package ba.unsa.etf.presenters;


import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.ProgressBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static ba.unsa.etf.GluonApplication.LOGIN_VIEW;
public class PasswordRecoveryPresenter {

    @FXML
    private View recoveryView;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextField usernameOrEmail;

    public void checkUsernameEmail() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/recover/securityquestion",
                    "{\"usernameOrEmail\":\"" + usernameOrEmail.getText() + "\"}", false);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                progressBar.progressProperty().set(0.66);
                Label label = new Label("Answer your security question: " + httpResponse.getMessage().getJsonObject(0).getString("title"));
                label.setWrapText(true);
                label.setAlignment(Pos.CENTER);
                TextField answer = new TextField();
                Button button = new Button("RECOVER PASSWORD");
                button.setOnAction(event -> recoverPassword(usernameOrEmail.getText(), answer.getText()));
                VBox center = new VBox(label, answer, button);
                center.setSpacing(20);
                center.setPadding(new Insets(20, 40, 0, 40));
                center.fillWidthProperty().set(true);
                center.setAlignment(Pos.TOP_CENTER);
                recoveryView.setCenter(center);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void recoverPassword(String usernameOrEmail, String answer) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/recover/newPassword",
                    "{\"usernameOrEmail\":\"" + usernameOrEmail + "\",\"answer\":\"" + answer + "\"}", false);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                if(httpResponse.getMessage().getJsonObject(0).getBoolean("success")) {
                    progressBar.progressProperty().set(1.0);
                    Label success = new Label("Your new password is: " + httpResponse.getMessage().getJsonObject(0).getString("password"));
                    success.setWrapText(true);
                    success.setAlignment(Pos.CENTER);
                    Button backToLogin = new Button("LOG IN");
                    backToLogin.setOnAction(event -> MobileApplication.getInstance().switchView(LOGIN_VIEW));
                    VBox vBox = new VBox(success, backToLogin);
                    vBox.setPadding(new Insets(20, 40, 0 ,40));
                    vBox.fillWidthProperty().set(true);
                    vBox.setSpacing(20);
                    vBox.setAlignment(Pos.TOP_CENTER);
                    recoveryView.setCenter(vBox);
                } else {
                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "Wrong answer!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }
}
