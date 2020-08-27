package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.models.ParkingLot;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.AutoCompleteTextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePresenter {

    @FXML
    private View homepage;

    @FXML
    AutoCompleteTextField<ParkingLot> searchParking;

    public void initialize() {
        homepage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> GluonApplication.menu.open()));
            }
        });


        searchParking.setCompleter(s -> {
            List<ParkingLot> persons = Arrays.asList(new ParkingLot("613", 39), new ParkingLot("124", 23));
            return persons;
        });
        searchParking.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("New value selected from the auto-complete popup: " + newValue);
        });
    }


}
