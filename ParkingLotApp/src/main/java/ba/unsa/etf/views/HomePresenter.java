package ba.unsa.etf.views;

import ba.unsa.etf.GluonApplication;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;

public class HomePresenter {

    @FXML
    private View homepage;

    public void initialize() {
        homepage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> GluonApplication.menu.open()));
            }
        });

    }
}
