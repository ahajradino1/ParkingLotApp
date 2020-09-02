package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.*;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.DatePicker;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Callback;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ba.unsa.etf.GluonApplication.*;

public class HomePresenter {

    @FXML
    private View homepage;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchParking;

    private final ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList();
    private final ObservableList<BankAccount> bankAccounts = FXCollections.observableArrayList();
    private final ObservableList<RegistrationPlate> registrationPlates = FXCollections.observableArrayList();
    private final CharmListView<ParkingLot, Integer> charmListView = new CharmListView<>();
    private LocalDate pickedDate;
    private LocalTime pickedTime;
    private LocalDateTime currentDateTime, pickedDateTime;
    private BankAccount chosenBankAccount;
    private RegistrationPlate chosenRegistrationPlate;
    private Double price;

    public void initialize() {
        homepage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> GluonApplication.menu.open()));
                appBar.setTitleText("Reserve parking spot");
            }
        });
    }

    public void onShown() {
        setListItems();
        LocalDateTime withSecondsAndNano = LocalDateTime.now();
        currentDateTime = withSecondsAndNano.withNano(0);
        currentDateTime = currentDateTime.withSecond(0);
        searchParking();
        onItemSelected();
        getBankAccounts();
        getRegistrationPlates();
    }

    public void setListItems() {
        getParkingLots();
        if(parkingLots.size() != 0) {
            charmListView.setItems(parkingLots);
            charmListView.setCellFactory(new Callback<CharmListView<ParkingLot, Integer>, CharmListCell<ParkingLot>>() {
                @Override
                public CharmListCell<ParkingLot> call(CharmListView<ParkingLot, Integer> param) {
                    return new CharmListCell<ParkingLot>() {
                        @Override
                        public void updateItem(ParkingLot item, boolean empty) {
                            super.updateItem(item, empty);
                            if(item != null && !empty) {
                                ListTile tile = new ListTile();
                                ImageView imageView = new ImageView(new Image(GluonApplication.class.getResourceAsStream("images/parking-icon.png")));
                                imageView.setFitHeight(20.0);
                                imageView.setFitWidth(20.0);
                                tile.setPrimaryGraphic(imageView);
                                tile.textProperty().setAll("Street address: " + item.getStreetAddress() + "\nMunicipality: " + item.getMunicipality());
                                setText(null);
                                setGraphic(tile);
                            }
                        }

                    };
                }
            });
            scrollPane.setContent(charmListView);

        } else {
            ImageView noData = new ImageView(new Image(GluonApplication.class.getResourceAsStream("images/no_data.png")));
            scrollPane.setContent(noData);
        }
    }

    public void searchParking() {
        FilteredList<ParkingLot> filteredData = new FilteredList<>(parkingLots, p -> true);

        searchParking.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(parkingLot ->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(parkingLot.getStreetAddress().toLowerCase().contains(lowerCaseFilter)) return true;
                else return parkingLot.getMunicipality().toLowerCase().contains(lowerCaseFilter);
            });
        });
        charmListView.setItems(filteredData);
    }

    public void getParkingLots() {
        parkingLots.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("parkinglots/all", false);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                JsonArray dbParkingLots = httpResponse.getMessage();
                for(int i = 0; i < dbParkingLots.size(); i++) {
                    JsonObject parkingLot = dbParkingLots.getJsonObject(i);
                    parkingLots.add(new ParkingLot(parkingLot.getJsonNumber("id").longValue(), parkingLot.getString("zoneCode"), parkingLot.getString("streetAddress"), parkingLot.getString("municipality"), parkingLot.getJsonNumber("price").doubleValue()));
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onItemSelected() {
        charmListView.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Dialog dialog = new Dialog(true);
            dialog.setTitleText("");
            dialog.setContent(dialogContent(newValue));
            Button payButton = new Button("PAY");
            payButton.setOnAction(event -> {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "By continuing you will be charged " + price + " KM for selected parking spot.");
                alert.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {
                        proceedWithPayment(newValue.getId(), chosenBankAccount.getId(), chosenRegistrationPlate.getId(), currentDateTime.toString() + "+02:00", pickedDateTime.toString() + "+02:00", price);
                    }
                });
            });
            dialog.getButtons().addAll(payButton);

            dialog.showAndWait();
        });
    }

    private void proceedWithPayment(Long parkingLotId,
                                    Long bankAccountId,
                                    Long registrationPlateId,
                                    String startingTime,
                                    String endingTime,
                                    Double price) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/payments/submit",
                    "{\"parkingLotId\":\"" + parkingLotId
                            + "\",\"bankAccountId\":\"" + bankAccountId
                            + "\",\"registrationPlateId\":\"" + registrationPlateId
                            + "\",\"startingTime\":\"" + startingTime
                            + "\",\"endingTime\":\"" + endingTime
                            + "\",\"price\":\"" + price + "\"}", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                MobileApplication.getInstance().switchView(PAYMENT_SUCCESS_VIEW);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VBox dialogContent(ParkingLot parkingLot) {
        VBox container = new VBox();

        //location details
        BorderPane location = new BorderPane();
        Icon locationIcon = new Icon(MaterialDesignIcon.LOCATION_ON);
        location.setLeft(locationIcon);
        VBox vBox = new VBox(new Label(parkingLot.getStreetAddress()), new Label(parkingLot.getMunicipality()));
        vBox.setAlignment(Pos.CENTER);
        location.setCenter(vBox);
        location.setRight(new VBox(new Label("Zone code"), new Label(parkingLot.getZoneCode())));
        location.setPadding(new Insets(0, 10, 0, 10));


        //working time and price per hour
        BorderPane parkingDetails = new BorderPane();
        parkingDetails.setLeft(new Label("Every day"));
        parkingDetails.setCenter(new Label("00:00-24:00"));
        parkingDetails.setRight(new Label(parkingLot.getCostPerHour() + " KM/h"));
        parkingDetails.setPadding(new Insets(0, 10, 0, 10));

        //reserved time and total price
        BorderPane ticketDetails = new BorderPane();
        VBox labels = new VBox(new Label("Starting time"), new Label("Ending time"), new Label("Total cost"));
        labels.setSpacing(23);
        labels.setAlignment(Pos.CENTER_LEFT);
        ticketDetails.setLeft(labels);

        Label priceLabel = new Label("0 KM");
        Button date = new Button();
        Button time = new Button();
        Label dateTimeValidator = new Label("Please pick correct date and time.");
        dateTimeValidator.setVisible(false);
        dateTimeValidator.setStyle("-fx-text-fill: red;-fx-font-size: 10");

        date.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               pickedDate = openDateDialog(date);
               setPrice(pickedTime != null, dateTimeValidator, priceLabel, parkingLot);
            }
        });
        date.setStyle("-fx-background-color: white;-fx-text-fill: black");
        date.setGraphic(new Icon(MaterialDesignIcon.PERM_CONTACT_CALENDAR));

        time.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pickedTime = openTimeDialog(time);
                setPrice(pickedDate != null, dateTimeValidator, priceLabel, parkingLot);
            }
        });
        time.setStyle("-fx-background-color: white;-fx-text-fill: black");
        time.setGraphic(new Icon(MaterialDesignIcon.TIMER));
        HBox dateTime = new HBox(date, time);
        dateTime.setSpacing(5);

        VBox rightSide = new VBox(new Label(currentDateTime.toString().replace('T', ' ')), dateTime, dateTimeValidator, priceLabel);
        rightSide.setSpacing(10);
        ticketDetails.setRight(rightSide);
        ticketDetails.setPadding(new Insets(0, 10, 0, 10));

        Node pickBankAcc = null;
        if(bankAccounts.size() == 0) {
            Icon icon = new Icon(MaterialDesignIcon.ERROR);
            Button addAccBtn = new Button("Add account");
            addAccBtn.setOnAction(event -> MobileApplication.getInstance().switchView(ADD_BANK_ACCOUNT_VIEW));
            VBox addAccContainer = new VBox(new Label("No bank accounts added."), addAccBtn);
            addAccContainer.setAlignment(Pos.CENTER);
            addAccContainer.setSpacing(10);
            HBox noAccount = new HBox(icon, addAccContainer);
            noAccount.setAlignment(Pos.CENTER);
            noAccount.setSpacing(15);
            pickBankAcc = noAccount;
        } else {
            ComboBox<BankAccount> comboBox = new ComboBox<>();
            comboBox.setPromptText("Choose credit card");
            comboBox.setVisibleRowCount(3);
            comboBox.setItems(bankAccounts);
            comboBox.setStyle("-fx-background-color: bisque");
            comboBox.setPrefHeight(40);
            comboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BankAccount>() {
                @Override
                public void changed(ObservableValue<? extends BankAccount> observable, BankAccount oldValue, BankAccount newValue) {
                    chosenBankAccount = newValue;
                }
            });
            pickBankAcc = comboBox;
        }
        Node pickRegistrationPlate = null;
        if(registrationPlates.size() == 0) {
            Icon icon = new Icon(MaterialDesignIcon.ERROR);
            Button addRegPlate = new Button("Add registration plate");
            addRegPlate.setOnAction(e -> MobileApplication.getInstance().switchView(ADD_REG_PLATE_VIEW));
            VBox addPlateContainer = new VBox(new Label("No registration plates added."), addRegPlate);
            addPlateContainer.setAlignment(Pos.CENTER);
            addPlateContainer.setSpacing(10);
            HBox noPlate = new HBox(icon, addPlateContainer);
            noPlate.setAlignment(Pos.CENTER);
            noPlate.setSpacing(15);
            pickRegistrationPlate = noPlate;
        } else {
            ComboBox<RegistrationPlate> comboBox = new ComboBox<>();
            comboBox.setPromptText("Choose car");
            comboBox.setVisibleRowCount(3);
            comboBox.setItems(registrationPlates);
            comboBox.setStyle("-fx-background-color: bisque");
            comboBox.setPrefHeight(40);
            comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> chosenRegistrationPlate = newValue);
            pickRegistrationPlate = comboBox;
        }
        container.getChildren().setAll(location, createSeparator(), parkingDetails, createSeparator(), ticketDetails, createSeparator(), pickBankAcc, createSeparator(), pickRegistrationPlate);
        container.setSpacing(10);
        container.setAlignment(Pos.TOP_CENTER);
        return container;
    }

    private void setPrice(boolean b, Label dateTimeValidator, Label priceLabel, ParkingLot parkingLot) {
        if(b) {
            pickedDateTime = LocalDateTime.of(pickedDate, pickedTime);
            if(!validateDateAndTime()) {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, "You have chosen date in the past! Please pick correct date and time.");
                alert.showAndWait();
                dateTimeValidator.setVisible(true);
            } else {
                price = countPrice(currentDateTime, pickedDateTime, parkingLot.getCostPerHour());
                priceLabel.setText(price + " KM");
                dateTimeValidator.setVisible(false);
            }
        }
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setPadding(new Insets(0, 10, 0, 10));
        separator.setOpacity(0.7);
        return separator;
    }

    public LocalDate openDateDialog(Button button) {
        DatePicker datePicker = new DatePicker();
        datePicker.showAndWait().ifPresent(System.out::println);
        LocalDate chosenDate = datePicker.dateProperty().getValue();
        button.setText(chosenDate.toString());
        return chosenDate;
    }

    public LocalTime openTimeDialog(Button button) {
        TimePicker timePicker = new TimePicker();
        timePicker.showAndWait().ifPresent(System.out::println);
        LocalTime chosenTime = timePicker.timeProperty().getValue();
        button.setText(chosenTime.toString());
        return chosenTime;
    }

    private boolean validateDateAndTime() {
        return currentDateTime.isBefore(pickedDateTime);
    }

    private Double countPrice(LocalDateTime start, LocalDateTime end, Double pricePerHour) {
        return start.until(end, ChronoUnit.HOURS) * pricePerHour;
    }

    private void getBankAccounts() {
        bankAccounts.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/accounts/all", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                JsonArray dbBankAccounts = httpResponse.getMessage();
                for(int i = 0; i < dbBankAccounts.size(); i++) {
                    JsonObject bankAccount = dbBankAccounts.getJsonObject(i);
                    bankAccounts.add(new BankAccount((long)bankAccount.getInt("id"), bankAccount.getString("accountOwner"), bankAccount.getString("bankName"), bankAccount.getString("expiryDate").substring(0, 10), bankAccount.getString("cardNumber")));
                }
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getRegistrationPlates() {
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
}
