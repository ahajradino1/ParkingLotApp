package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.BankAccount;
import ba.unsa.etf.models.Ticket;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;

import static ba.unsa.etf.GluonApplication.PRIMARY_VIEW;
import static ba.unsa.etf.GluonApplication.SECONDARY_VIEW;

public class TicketsPresenter {
    @FXML
    private View tickets;

    @FXML
    private ScrollPane scrollPane;

    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final ObservableList<Ticket> activeTickets = FXCollections.observableArrayList();

    public void initialize() {
        tickets.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Tickets");
            }
        });

        BottomNavigation bottomNavigation = new BottomNavigation();
        BottomNavigationButton btn1 = new BottomNavigationButton("All tickets", MaterialDesignIcon.LIST.graphic(), e -> tickets.setCenter(new Button("neki button")));
        BottomNavigationButton btn2 = new BottomNavigationButton("Active tickets", MaterialDesignIcon.TIMER.graphic(), e -> tickets.setCenter(new Label("Valjda moze ovako")));
        bottomNavigation.getActionItems().addAll(btn1, btn2);
        tickets.setBottom(bottomNavigation);
        MapView mapView = new MapView();
        mapView.flyTo(0, new MapPoint(43.2692707,19.9935308), 10);
        scrollPane.setContent(mapView);
    }
//
//    public Node showAllTickets() {
//
//    }
//
//    public Node showActiveTickets() {
//
//    }

    public ExpansionPanelContainer createPanel(boolean all) {
        ExpansionPanelContainer panelContainer = new ExpansionPanelContainer();



        return panelContainer;
    }

    public void getAllTickets() {
        allTickets.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/tickets/all", true);
            if(httpResponse.getCode() == 200) {
                JsonArray dbTickets = httpResponse.getMessage();
                for(int i = 0; i < dbTickets.size(); i++) {
                    JsonObject ticket = dbTickets.getJsonObject(i);
                    JsonObject parkingLotObject = ticket.getJsonObject("parkingLot");

                   //allTickets.add(new Ticket(ticket.getString("ticketId"), ticket.getString("cardNumber"), ticket.getString("registrationNumber"), parkingLot, ticket.getString("startingTime"), ticket.getString("endingTime"), ticket.getJsonNumber("price").doubleValue()));
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
