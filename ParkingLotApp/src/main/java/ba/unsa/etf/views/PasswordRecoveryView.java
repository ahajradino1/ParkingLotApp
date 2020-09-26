package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class PasswordRecoveryView {

    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("recover_password.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
