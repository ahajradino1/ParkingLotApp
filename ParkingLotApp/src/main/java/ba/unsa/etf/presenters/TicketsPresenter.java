package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.ParkingLot;
import ba.unsa.etf.models.Ticket;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.Dialog;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;


public class TicketsPresenter {
    @FXML
    private View tickets;

    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final ObservableList<Ticket> activeTickets = FXCollections.observableArrayList();
    private final CharmListView<Ticket, Integer> charmListView = new CharmListView();
    private BottomNavigation bottomNavigation;

    public void initialize() {
        tickets.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Tickets");
            }
        });
        bottomNavigation = new BottomNavigation();
        getAllTickets();
        getActiveTickets();
        BottomNavigationButton btn1 = new BottomNavigationButton("All tickets", MaterialDesignIcon.LIST.graphic(), e -> setListItems(true));
        BottomNavigationButton btn2 = new BottomNavigationButton("Active tickets", MaterialDesignIcon.TIMER.graphic(), e -> setListItems(false));
        bottomNavigation.getActionItems().addAll(btn1, btn2);
        tickets.setBottom(bottomNavigation);
        bottomNavigation.getActionItems().get(0).fire();
        onItemSelected();
    }

    public void onShown() {
        getAllTickets();
        getActiveTickets();
        bottomNavigation.getActionItems().get(0).fire();
    }

    public void setListItems(boolean all) {
        if(all && allTickets.size() != 0) {
            charmListView.setItems(allTickets);
            customizeListItem(true);
        } else if(!all && activeTickets.size() != 0) {
            charmListView.setItems(activeTickets);
            customizeListItem(false);
        } else {
            ImageView noData = new ImageView(new Image(GluonApplication.class.getResourceAsStream("images/no data.png")));
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
                            Image imgFile;
                            if(all) {
                                imgFile = new Image(GluonApplication.class.getResourceAsStream("images/ticket.png"));
                            } else
                                imgFile = new Image(GluonApplication.class.getResourceAsStream("images/active-ticket.png"));
                            ImageView imageView = new ImageView(imgFile);
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
            dialog.setTitleText("Ticket details");
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

        VBox rightSide = new VBox(new Label(ticket.getStartingTime()), new Label(ticket.getEndingTime()), new Label(ticket.getPrice() + " KM"));
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
        if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
            JsonArray dbTickets = httpResponse.getMessage();
            for(int i = 0; i < dbTickets.size(); i++) {
                JsonObject ticket = dbTickets.getJsonObject(i);
                JsonObject parkingLotObject = ticket.getJsonObject("parkingLot");
                ParkingLot parkingLot = new ParkingLot(parkingLotObject.getJsonNumber("id").longValue(), parkingLotObject.getString("zoneCode"), parkingLotObject.getString("streetAddress"), parkingLotObject.getString("municipality"), parkingLotObject.getJsonNumber("price").doubleValue());
                activeTickets.add(new Ticket(ticket.getString("ticketId"), ticket.getString("cardNumber"), ticket.getString("registrationNumber"), parkingLot, ticket.getString("startingTime"), ticket.getString("endingTime"), ticket.getJsonNumber("price").doubleValue()));
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
