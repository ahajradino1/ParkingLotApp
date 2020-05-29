package ba.unsa.etf.views;

import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;


import java.io.IOException;


public class LoginPresenter {
    public static String TOKEN;
    @FXML
    private View login;
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
        System.out.println("Registruj se");
        System.out.println("java version: "+System.getProperty("java.version"));
        System.out.println("javafx.version: " + System.getProperty("javafx.version"));
    }

    public void login(MouseEvent mouseEvent) {
        try {
            HttpResponse httpResponse = HttpUtils.POST("api/auth/signin", "{\"usernameOrEmail\":\"" + username.getText()+ "\",\"password\":\"" + password.getText() +"\"}", false);
            System.out.println(httpResponse.getCode());
            System.out.println(httpResponse.getMessage());
            if(httpResponse.getCode() == 200)
                //todo ovdje dodijelim vrijednost TOKENu i preusmjerim na HomePage (to ce biti sa drawer-om)
                //todo napraviti homepage sa drawerom
                TOKEN = httpResponse.getMessage().getString("accessToken");
            //todo provjeriti slucaj kad code nije 200 i ispisati poruku u nekom popup-u
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
