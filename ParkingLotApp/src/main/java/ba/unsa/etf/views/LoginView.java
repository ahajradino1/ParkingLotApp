package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class LoginView {

    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("login.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}



