package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SignupView {

    public View getView() {
        try {
            View view = FXMLLoader.load(LoginView.class.getResource("signup.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
