package ba.unsa.etf.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class AddBankAccSuccessView {
    public View getView() {
        try {
            View view = FXMLLoader.load(LoginView.class.getResource("add_bank_acc_success.fxml"));
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
