package ba.unsa.etf.views.signup;

import ba.unsa.etf.views.LoginView;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SignupView {

    public View getView() {
        try {
            View view = FXMLLoader.load(LoginView.class.getResource("signup/signup.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
