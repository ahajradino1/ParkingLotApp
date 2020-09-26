package ba.unsa.etf.presenters;

import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.io.IOException;

import static ba.unsa.etf.GluonApplication.*;


public class LoginPresenter {
    public static String TOKEN;


    @FXML
    private View loginView;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    public void initialize() {
        loginView.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setVisible(false);
            }
        });
    }

    public void register() {
        MobileApplication.getInstance().switchView(SIGNUP_VIEW);
    }

    public void login() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/auth/signin", "{\"usernameOrEmail\":\"" + username.getText()+ "\",\"password\":\"" + password.getText() +"\"}", false);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                TOKEN = httpResponse.getMessage().getJsonObject(0).getString("accessToken");
                 MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }

    public void forgotPassword() {
        MobileApplication.getInstance().switchView(PASSWORD_RECOVERY_VIEW);
    }
}
