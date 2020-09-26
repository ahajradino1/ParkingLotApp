package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class HomeView {

    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("homepage.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
