package ba.unsa.etf.presenters.registration_plates;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.RegistrationPlate;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;

import static ba.unsa.etf.GluonApplication.ADD_REG_PLATE_VIEW;

public class RegistrationPlatePresenter {

    @FXML
    private View registrationPlatesView;

    private final ObservableList<RegistrationPlate> registrationPlates = FXCollections.observableArrayList();

    public void initialize() {
        registrationPlatesView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Registration plates");
            }
        });
    }

    public void setItems() {
        getRegistrationPlates();
        if(registrationPlates.size() != 0) {
            CharmListView<RegistrationPlate, Integer> charmListView = new CharmListView<>();
            charmListView.setItems(registrationPlates);
            charmListView.setCellFactory(new Callback<CharmListView<RegistrationPlate, Integer>, CharmListCell<RegistrationPlate>>() {
                @Override
                public CharmListCell<RegistrationPlate> call(CharmListView<RegistrationPlate, Integer> param) {
                    return new CharmListCell<RegistrationPlate>() {
                        @Override
                        public void updateItem(RegistrationPlate item, boolean empty) {
                            super.updateItem(item, empty);
                            if(item != null && !empty){
                                Button deleteBtn = MaterialDesignIcon.DELETE.button();
                                deleteBtn.setOnAction(event -> {
                                    Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this registration plate?");
                                    alert.showAndWait().ifPresent(result -> {
                                        if (result == ButtonType.OK) {
                                            deletePlate(item.getId());
                                            charmListView.itemsProperty().remove(item);
                                        }
                                    });
                                });
                                deleteBtn.setStyle("-fx-text-fill: red");
                                ListTile tile = new ListTile();
                                tile.setSecondaryGraphic(new VBox(deleteBtn));
                                tile.textProperty().setAll("Registration number", item.getRegistrationNumber());
                                setGraphic(tile);
                            } else {
                                setText(null);
                                setGraphic(null);
                            }
                        }
                    };
                }
            });
            registrationPlatesView.setCenter(charmListView);
        } else {
            ImageView noData = new ImageView(new Image(GluonApplication.class.getResourceAsStream("images/no data.png")));
            registrationPlatesView.setCenter(noData);
        }
    }

    private void deletePlate(Long plateId) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.DELETE("api/plates/delete/" + plateId, true);
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION, httpResponse.getMessage().getJsonObject(0).getString("text"));
            if (httpResponse.getCode() != 200 || httpResponse.getCode() != 201)
                alert.setAlertType(javafx.scene.control.Alert.AlertType.ERROR);
            alert.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addRegistrationPlates() {
        MobileApplication.getInstance().switchView(ADD_REG_PLATE_VIEW);
    }

    public void getRegistrationPlates() {
        registrationPlates.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/plates/all", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                JsonArray dbRegPlates = httpResponse.getMessage();
                for(int i = 0; i < dbRegPlates.size(); i++) {
                    JsonObject regPlate = dbRegPlates.getJsonObject(i);
                    registrationPlates.add(new RegistrationPlate((long)regPlate.getInt("id"), regPlate.getString("registrationNumber")));
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onShown() {
        setItems();
    }
}
