package ba.unsa.etf.presenters.bank_accounts;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.BankAccount;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.*;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;

import static ba.unsa.etf.GluonApplication.ADD_BANK_ACCOUNT_VIEW;

public class BankAccountsPresenter {

    @FXML
    private ScrollPane scrollView;

    @FXML
    private View bankAccountsView;

    private ObservableList<BankAccount> bankAccounts = FXCollections.observableArrayList();

    public void initialize() {
        bankAccountsView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e ->
                        GluonApplication.menu.open()));
                appBar.setTitleText("Bank accounts");
            }
        });
    }

    public void setItems() {
        getBankAccounts();
        if(bankAccounts.size() != 0) {
            ExpansionPanelContainer accountsContainer = new ExpansionPanelContainer();
            for (int i = 0; i < bankAccounts.size(); i++) {
                BankAccount account = bankAccounts.get(i);
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
                        Alert alert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION, "Are you sure you want to \n delete this bank account?");
                        alert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                //send delete request
                                deleteAccount(account.getId());
                                accountsContainer.getItems().remove(panel);
                            }
                        });

                    }
                });

                GridPane accountDetails = new GridPane();
                accountDetails.add(new Label("Account owner: "), 0, 0);
                accountDetails.add(new Label(account.getAccountOwner()), 1, 0);
                accountDetails.add(new Label("Credit card number: "), 0, 1);
                accountDetails.add(new Label(account.getCardNumber()), 1, 1);
                accountDetails.add(new Label("Expiry date: "), 0, 2);
               // Calendar date = Calendar.getInstance();
                //date.setTime(account.getExpiryDate());
              //  accountDetails.add(new Label(date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR)), 1, 2);
                accountDetails.add(new Label(account.getExpiryDate()), 1, 2);
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
            scrollView.setContent(accountsContainer);
        } else {
            ImageView noData = new ImageView(new File("src/main/resources/ba/unsa/etf/images/no_data.png").toURI().toString());
            noData.setFitWidth(100);
            noData.setFitWidth(100);
            scrollView.setContent(noData);
        }
    }

    public void getBankAccounts() {
        bankAccounts.clear();
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/accounts/all", true);
            if(httpResponse.getCode() == 200) {
                JsonArray dbBankAccounts = httpResponse.getMessage();
                for(int i = 0; i < dbBankAccounts.size(); i++) {
                    JsonObject bankAccount = dbBankAccounts.getJsonObject(i);
                    //Date expiryDate = new SimpleDateFormat("yyyy-dd-MM").parse(bankAccount.getString("expiryDate"));
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

    public void addAccount() {
        MobileApplication.getInstance().switchView(ADD_BANK_ACCOUNT_VIEW);
    }

    public void deleteAccount(Long accountId) {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.DELETE("api/accounts/delete/" + accountId, true);
            Alert alert = new Alert(javafx.scene.control.Alert.AlertType.INFORMATION, httpResponse.getMessage().getJsonObject(0).getString("text"));
            if (httpResponse.getCode() != 200)
                alert.setAlertType(javafx.scene.control.Alert.AlertType.ERROR);
            alert.showAndWait();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
