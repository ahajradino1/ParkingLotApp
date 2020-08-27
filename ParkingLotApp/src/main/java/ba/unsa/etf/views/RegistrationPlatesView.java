package ba.unsa.etf.views;

import ba.unsa.etf.GluonApplication;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class RegistrationPlatesView {
    public View getView() {
        try {
            View view = FXMLLoader.load(LoginView.class.getResource("registration_plates.fxml"));
            view.setBottom(GluonApplication.bottomNavigation);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
