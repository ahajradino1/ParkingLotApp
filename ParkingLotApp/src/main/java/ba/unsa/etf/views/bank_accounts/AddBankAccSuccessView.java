package ba.unsa.etf.views.bank_accounts;

import ba.unsa.etf.views.LoginView;
import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class AddBankAccSuccessView {
    public View getView() {
        try {
            return FXMLLoader.load(LoginView.class.getResource("bank_accounts/add_bank_acc_success.fxml"));
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            return new View();
        }
    }
}
