package ba.unsa.etf.presenters;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.models.BankAccount;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.ExpansionPanel;
import com.gluonhq.charm.glisten.control.ExpansionPanelContainer;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static ba.unsa.etf.GluonApplication.ADD_BANK_ACCOUNT_VIEW;

public class BankAccountsPresenter {
    @FXML
    private View bankAccountsView;

    @FXML
    private ExpansionPanelContainer accountsContainer;

    private List<BankAccount> bankAccounts = new ArrayList<>();

    public void initialize() {
        bankAccountsView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Bank accounts");
            }
        });
      //  getBankAccounts();
        setItems();
    }

    public void setItems() {
        getBankAccounts();
        for (BankAccount account : bankAccounts) {
            ExpansionPanel panel = new ExpansionPanel();

            Label bankName = new Label(account.getBankName());
            Label cardNumber = new Label(account.getCardNumber());
            ExpansionPanel.CollapsedPanel collapsedPanel = new ExpansionPanel.CollapsedPanel();
            collapsedPanel.getTitleNodes().addAll(bankName, cardNumber);

           // Label expiryDate = new Label(account.getExpiryDate().toString());
            Button deleteBtn = new Button("DELETE");
            deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //send delete request
                }
            });

            GridPane accountDetails = new GridPane();
            accountDetails.add(new Label("Account owner: "), 0, 0);
            accountDetails.add(new Label(account.getAccountOwner()), 1, 0);
            accountDetails.add(new Label("Credit card number: "), 0, 1);
            accountDetails.add(new Label(account.getCardNumber()), 1, 1);
            accountDetails.add(new Label("Expiry date: "), 0, 2);
            Calendar date = Calendar.getInstance();
            date.setTime(account.getExpiryDate());
            accountDetails.add(new Label(date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR)), 1, 2);
            accountDetails.add(new Label("Bank name: "), 0, 3);
            accountDetails.add(new Label(account.getBankName()), 1, 3);
            accountDetails.add(deleteBtn, 1, 4);
            accountDetails.setAlignment(Pos.CENTER);
            accountDetails.setHgap(5);
            accountDetails.setVgap(5);
            accountDetails.setPadding(new Insets(5, 0, 5, 0));


            ExpansionPanel.ExpandedPanel expandedPanel = new ExpansionPanel.ExpandedPanel();
            expandedPanel.setContent(accountDetails);


            panel.setExpandedContent(expandedPanel);
            panel.setCollapsedContent(collapsedPanel);
            accountsContainer.getItems().add(panel);
        }
    }

    public void getBankAccounts() {
        BankAccount b1 = new BankAccount((long) 1, "Ajsa", "Unicredit", new Date(), "1234567891234567");
        BankAccount b2 = new BankAccount((long) 2, "Haris", "Unicredit", new Date(), "1234567891234547");
        BankAccount b3 = new BankAccount((long) 3, "Ajsa H", "Unicredit", new Date(), "1234567891234537");
        BankAccount b4 = new BankAccount((long) 4, "Ajsaa", "Unicredit", new Date(), "1234567891234527");
        BankAccount b5 = new BankAccount((long) 5, "Ajsa", "Unicredit", new Date(), "1234567891234517");
        BankAccount b6 = new BankAccount((long) 6, "Ajsa", "Unicredit", new Date(), "1234567891234507");
        BankAccount b7 = new BankAccount((long) 7, "Ajsa", "Unicredit", new Date(), "1234567891234587");
        BankAccount b8 = new BankAccount((long) 8, "Ajsa", "Unicredit", new Date(), "1234567891234597");
        bankAccounts.add(b1);
        bankAccounts.add(b2);
        bankAccounts.add(b3);
        bankAccounts.add(b4);
        bankAccounts.add(b5);
        bankAccounts.add(b6);
        bankAccounts.add(b7);
        bankAccounts.add(b8);
    }


    public void addAccount() {
        MobileApplication.getInstance().switchView(ADD_BANK_ACCOUNT_VIEW);
    }
}
