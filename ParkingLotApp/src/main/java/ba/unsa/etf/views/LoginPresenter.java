package ba.unsa.etf.views;

import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;


import java.io.IOException;

import static ba.unsa.etf.GluonApplication.SIGNUP_VIEW;


public class LoginPresenter {
    public static String TOKEN;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

//    public void initialize() {
//        login.showingProperty().addListener(((observable, oldValue, newValue) -> {
//            if(newValue) {
//                AppBar appBar = MobileApplication.getInstance().getAppBar();
//                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
//                        MobileApplication.getInstance().getDrawer().open()));
//                appBar.setTitleText("Home");
//            }
//        }));
//    }

    public void register(MouseEvent mouseEvent) {
        System.out.println("registruj se");

        MobileApplication.getInstance().switchView(SIGNUP_VIEW);
    }

    public void login() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/auth/signin", "{\"usernameOrEmail\":\"" + username.getText()+ "\",\"password\":\"" + password.getText() +"\"}", false);
            System.out.println(httpResponse.getCode());
            System.out.println(httpResponse.getMessage());
            if(httpResponse.getCode() == 200)
                //todo ovdje dodijelim vrijednost TOKENu i preusmjerim na HomePage (to ce biti sa drawer-om)
                //todo napraviti homepage sa drawerom
                TOKEN = httpResponse.getMessage().getJsonObject(0).getString("accessToken");
            //todo provjeriti slucaj kad code nije 200 i ispisati poruku u nekom popup-u
            else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
           System.out.println(e.getMessage());
        }
    }
}
