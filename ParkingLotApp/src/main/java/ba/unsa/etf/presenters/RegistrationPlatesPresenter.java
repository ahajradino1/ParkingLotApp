package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;

public class RegistrationPlatesPresenter {
    @FXML
    private View registrationPlates;

    public void initialize() {
        registrationPlates.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Registration plates");
            }
        });
    }
}
