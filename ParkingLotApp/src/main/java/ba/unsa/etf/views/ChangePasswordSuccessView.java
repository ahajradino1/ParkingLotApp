package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class ChangePasswordSuccessView {
    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("change_password_success.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
