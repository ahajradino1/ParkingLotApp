package ba.unsa.etf.presenters.registration_plates;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.LifecycleEvent;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

import static ba.unsa.etf.GluonApplication.ADD_BANK_ACC_SUCCESS_VIEW;
import static ba.unsa.etf.GluonApplication.ADD_REG_PLATE_SUCCESS_VIEW;

public class AddRegistrationPlatePresenter {

    @FXML
    private View addRegPlateView;

    @FXML
    private TextField registrationNumber;

    @FXML
    private Label regNumberValidator;

    @FXML
    private Button addPlateBtn;

    public void initialize() {
        addRegPlateView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(event -> GluonApplication.getInstance().switchToPreviousView()));
                appBar.setTitleText("Add registration plate");
            }
        });
    }

    public void addRegistrationPlate() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/plates/add",
                    "{\"registrationNumber\":\"" + registrationNumber.getText().toUpperCase() + "\"}", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                MobileApplication.getInstance().switchView(ADD_REG_PLATE_SUCCESS_VIEW);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void validateRegNumber() {
        registrationNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                regNumberValidator.setText("This field is required!");
                regNumberValidator.setVisible(true);
                addPlateBtn.setDisable(true);
            } else if (!isRegularPlate(newValue) && !isTaxiPlate(newValue) && !isTestPlate(newValue)) {
                regNumberValidator.setText("Registration number does not match template.");
                regNumberValidator.setVisible(true);
                addPlateBtn.setDisable(true);
            } else if (isRegularPlate(newValue) || isTaxiPlate(newValue) || isTestPlate(newValue)) {
                regNumberValidator.setVisible(false);
                addPlateBtn.setDisable(false);
            }
        });
    }

   // X00-X-000 (taxis: TA-000000)
    public boolean isRegularPlate(String plate) {
        return plate.matches("[a-zA-Z&&[^xXyYwWqQ]]\\d{2}-[a-zA-Z&&[^xXyYwWqQ]]-\\d{3}");
    }

    public boolean isTestPlate(String plate) {
        return plate.matches("[tT]{2}-\\d{6}");
    }

    public boolean isTaxiPlate(String plate) {
        return plate.matches("[tT][aA]-\\d{6}");
    }

}
