package ba.unsa.etf.views.change_password;

import ba.unsa.etf.views.LoginView;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ChangePasswordView {
    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("change_password/change_password.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
