package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.BankAccount;
import ba.unsa.etf.models.ParkingLot;
import ba.unsa.etf.models.RegistrationPlate;
import ba.unsa.etf.models.Ticket;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class TicketsPresenter {
    @FXML
    private View tickets;

    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final ObservableList<Ticket> activeTickets = FXCollections.observableArrayList();
    private final CharmListView<Ticket, Integer> charmListView = new CharmListView();

    public void initialize() {
        tickets.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Tickets");
            }
        });
    }

    public void onShown() {
        BottomNavigation bottomNavigation = new BottomNavigation();
        BottomNavigationButton btn1 = new BottomNavigationButton("All tickets", MaterialDesignIcon.LIST.graphic(), e -> setListItems(true));
        BottomNavigationButton btn2 = new BottomNavigationButton("Active tickets", MaterialDesignIcon.TIMER.graphic(), e -> setListItems(false));
        bottomNavigation.getActionItems().addAll(btn1, btn2);
        bottomNavigation.getActionItems().get(0).fire();
        tickets.setBottom(bottomNavigation);
        onItemSelected();
    }

    public void setListItems(boolean all) {
        getAllTickets();
        getActiveTickets();
        if(all && allTickets.size() != 0) {
            charmListView.setItems(allTickets);
            customizeListItem(all);
        } else if(!all && activeTickets.size() != 0) {
            charmListView.setItems(activeTickets);
            customizeListItem(all);
        } else {
            ImageView noData = new ImageView(new File("src/main/resources/ba/unsa/etf/images/no_data.png").toURI().toString());
            noData.setFitWidth(100);
            noData.setFitWidth(100);
            tickets.setCenter(noData);
        }
    }

    private void customizeListItem(boolean all) {
        charmListView.setCellFactory(new Callback<CharmListView<Ticket, Integer>, CharmListCell<Ticket>>() {
            @Override
            public CharmListCell<Ticket> call(CharmListView<Ticket, Integer> param) {
                return new CharmListCell<Ticket>() {
                    @Override
                    public void updateItem(Ticket item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null && !empty) {
                            ListTile tile = new ListTile();
                            File imgFile;
                            if(all) {
                                imgFile = new File("src/main/resources/ba/unsa/etf/images/ticket.png");
                            } else
                                imgFile = new File("src/main/resources/ba/unsa/etf/images/active-ticket.png");
                            ImageView imageView = new ImageView(imgFile.toURI().toString());
                            imageView.setFitHeight(20.0);
                            imageView.setFitWidth(20.0);
                            tile.setPrimaryGraphic(imageView);
                            tile.textProperty().setAll("Ticket id: " + item.getId() + "\nParked car: " + item.getRegistrationNumber());
                            setText(null);
                            setGraphic(tile);
                        }
                    }
                };
            }
        });
        tickets.setCenter(charmListView);
    }

    public void onItemSelected() {
        charmListView.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Dialog dialog = new Dialog(true);
            dialog.setTitleText("");
            dialog.setContent(dialogContent(newValue));
            dialog.showAndWait();
        });
    }

    public VBox dialogContent(Ticket ticket) {
        VBox container = new VBox();

        //location details
        BorderPane location = new BorderPane();
        Icon locationIcon = new Icon(MaterialDesignIcon.LOCATION_ON);
        location.setLeft(locationIcon);
        VBox vBox = new VBox(new Label(ticket.getParkingLot().getStreetAddress()), new Label(ticket.getParkingLot().getMunicipality()));
        vBox.setAlignment(Pos.CENTER);
        location.setCenter(vBox);
        location.setRight(new VBox(new Label("Zone code"), new Label(ticket.getParkingLot().getZoneCode())));
        location.setPadding(new Insets(0, 10, 0, 10));


        //working time and price per hour
        BorderPane parkingDetails = new BorderPane();
        parkingDetails.setLeft(new Label("Every day"));
        parkingDetails.setCenter(new Label("00:00-24:00"));
        parkingDetails.setRight(new Label(ticket.getParkingLot().getCostPerHour() + " KM/h"));
        parkingDetails.setPadding(new Insets(0, 10, 0, 10));

        //reserved time and total price
        BorderPane ticketDetails = new BorderPane();
        VBox labels = new VBox(new Label("Starting time"), new Label("Ending time"), new Label("Total cost"));
        labels.setSpacing(10);
        labels.setAlignment(Pos.CENTER_LEFT);
        ticketDetails.setLeft(labels);

        VBox rightSide = new VBox(new Label(ticket.getStartingTime().toString()), new Label(ticket.getEndingTime().toString()), new Label(ticket.getPrice() + " KM"));
        rightSide.setSpacing(10);
        ticketDetails.setRight(rightSide);
        ticketDetails.setPadding(new Insets(0, 10, 0, 10));


        container.getChildren().setAll(location, createSeparator(), parkingDetails, createSeparator(), ticketDetails, createSeparator(), new Label("Payed with bank account: " + ticket.getCardNumber()), createSeparator(), new Label("Parked car: " + ticket.getRegistrationNumber()));
        container.setSpacing(10);
        container.setAlignment(Pos.TOP_CENTER);
        return container;
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);
        separator.setPadding(new Insets(0, 10, 0, 10));
        separator.setOpacity(0.7);
        return separator;
    }

    private void getActiveTickets() {
        activeTickets.clear();
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.withNano(0);
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/tickets/active", "{\"currentDate\":\"" + localDateTime.withSecond(0).toString().replace('T', ' ')+ "\"}", true);
            getTickets(httpResponse, activeTickets);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getTickets(HttpResponse httpResponse, ObservableList<Ticket> activeTickets) throws ParseException {
        if(httpResponse.getCode() == 200) {
            JsonArray dbTickets = httpResponse.getMessage();
            for(int i = 0; i < dbTickets.size(); i++) {
                JsonObject ticket = dbTickets.getJsonObject(i);
                JsonObject parkingLotObject = ticket.getJsonObject("parkingLot");
                ParkingLot parkingLot = new ParkingLot(parkingLotObject.getJsonNumber("id").longValue(), parkingLotObject.getString("zoneCode"), parkingLotObject.getString("streetAddress"), parkingLotObject.getString("municipality"), parkingLotObject.getJsonNumber("price").doubleValue());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                format.setTimeZone(TimeZone.getTimeZone("CEST"));
                Date startingTime = format.parse(ticket.getString("startingTime"));
                Date endingTime = format.parse(ticket.getString("endingTime"));
                activeTickets.add(new Ticket(ticket.getString("ticketId"), ticket.getString("cardNumber"), ticket.getString("registrationNumber"), parkingLot, startingTime, endingTime, ticket.getJsonNumber("price").doubleValue()));
            }
        } else {
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
            alert.showAndWait();
        }
    }

    public void getAllTickets() {
        allTickets.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/tickets/all", true);
            getTickets(httpResponse, allTickets);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
    }

}