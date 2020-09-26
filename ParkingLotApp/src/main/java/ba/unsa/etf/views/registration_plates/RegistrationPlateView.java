package ba.unsa.etf.views.registration_plates;

import ba.unsa.etf.views.LoginView;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class RegistrationPlateView {
    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("registration_plates/registration_plates.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
