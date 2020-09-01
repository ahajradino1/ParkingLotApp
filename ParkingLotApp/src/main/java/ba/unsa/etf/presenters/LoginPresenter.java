package ba.unsa.etf.presenters;


import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.User;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import java.io.IOException;

import static ba.unsa.etf.GluonApplication.HOMEPAGE_VIEW;
import static ba.unsa.etf.GluonApplication.SIGNUP_VIEW;


public class LoginPresenter {
    public static String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDAiLCJpYXQiOjE1OTg3OTgxNzAsImV4cCI6MTU5OTY2MjE3MH0.K6AhHZGewIhm2PEQHtoehL_Ibo7wwtFc5j_azCaFmVVC0ECNDxYZCs8gNKP80Z7YGgPlPHDm0IgRcfuAzSJLsg";


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
        MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);

//        HttpResponse httpResponse = null;
//        try {
//            httpResponse = HttpUtils.POST("api/auth/signin", "{\"usernameOrEmail\":\"" + username.getText()+ "\",\"password\":\"" + password.getText() +"\"}", false);
//            if(httpResponse.getCode() == 200) {
//                //todo ovdje dodijelim vrijednost TOKENu i preusmjerim na HomePage (to ce biti sa drawer-om)
//                //todo napraviti homepage sa drawerom
//                TOKEN = httpResponse.getMessage().getJsonObject(0).getString("accessToken");
//                //todo provjeriti slucaj kad code nije 200 i ispisati poruku u nekom popup-u
//
//                MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);
//
//            } else {
//                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
//                alert.showAndWait();
//            }
//        } catch (IOException e) {
//           System.out.println(e.getMessage());
//        }
    }
}
