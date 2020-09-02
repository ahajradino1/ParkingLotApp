package ba.unsa.etf.views.signup;

import ba.unsa.etf.views.LoginView;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SignupSuccessView {

    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("signup/signup_success.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
