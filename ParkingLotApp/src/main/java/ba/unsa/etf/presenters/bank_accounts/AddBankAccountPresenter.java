package ba.unsa.etf.presenters.bank_accounts;

import ba.unsa.etf.GluonApplication;
import ba.unsa.etf.http.HttpResponse;
import ba.unsa.etf.http.HttpUtils;
import ba.unsa.etf.models.Bank;
import ba.unsa.etf.models.User;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Alert;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.DatePicker;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.time.LocalDate;

import static ba.unsa.etf.GluonApplication.ADD_BANK_ACC_SUCCESS_VIEW;


public class AddBankAccountPresenter {

    @FXML
    View addBankAccView;

    @FXML
    private Button addAccBtn;

    @FXML
    private TextField cvc;

    @FXML
    private TextField accountOwner;

    @FXML
    private TextField creditCardNumber;

    @FXML
    private ComboBox<Bank> comboBoxBanks;

    @FXML
    private Button openDialogBtn;

    @FXML
    private Label cardNumberValidator;

    @FXML
    private Label cvcValidator;

    @FXML
    private Label expiryDateValidator;

    private LocalDate chosenDate; //yyyy-mm-dd
    private User currentUser;
    private final ObservableList<Bank> bankList = FXCollections.observableArrayList();
    private Bank chosenBank;

    public void initialize() {
        addBankAccView.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(event -> GluonApplication.getInstance().switchToPreviousView()));
                appBar.setTitleText("Add bank account");
            }
        });
        getUserDetails();
        accountOwner.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        accountOwner.setDisable(true);
//        getBanks();
//        validateCardNumber();
//        validateCvc();
    }

    public void onShown() {
        getBanks();
        validateCardNumber();
        validateCvc();
    }

    public void addBankAccount() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.POST("api/accounts/add",
                    "{\"accountOwner\":\"" + accountOwner.getText()
                            + "\",\"cardNumber\":\"" + creditCardNumber.getText()
                            + "\",\"cvc\":\"" + cvc.getText()
                            + "\",\"expiryDate\":\"" + openDialogBtn.getText()
                            + "\",\"bankName\":\"" + chosenBank.getBankName()
                            + "\"}", true);
            if(httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
                MobileApplication.getInstance().switchView(ADD_BANK_ACC_SUCCESS_VIEW);
            } else {
                Alert alert = new Alert(javafx.scene.control.Alert.AlertType.ERROR, httpResponse.getMessage().getJsonObject(0).getString("message"));
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDialog() {
        DatePicker datePicker = new DatePicker();
        datePicker.showAndWait().ifPresent(System.out::println);
        chosenDate = datePicker.dateProperty().getValue();
        openDialogBtn.setText(chosenDate.toString());
        validateExpiryDate();
    }

    public void getUserDetails() {
        HttpResponse httpResponse = null;
        try {
            httpResponse = HttpUtils.GET("api/auth/user/me", true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (httpResponse != null && httpResponse.getCode() == 200 || httpResponse.getCode() == 201) {
            JsonObject userDetails = httpResponse.getMessage().getJsonObject(0);
            currentUser = new User((long) userDetails.getInt("id"),
                    userDetails.getString("username"),
                    userDetails.getString("firstName"),
                    userDetails.getString("lastName"),
                    userDetails.getString("email"));
        }
    }

    public void getBanks() {
        JsonArray dbBanks;
        try {
            HttpResponse httpResponse = HttpUtils.GET("banks", true);
            dbBanks = httpResponse.getMessage();
            convertToObservableList(dbBanks);
            comboBoxBanks.setPromptText("Choose bank");
            comboBoxBanks.getSelectionModel().selectedItemProperty()
                    .addListener((ChangeListener<Bank>) (observable, oldValue, newValue) -> chosenBank = newValue);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void validateCardNumber() {
        creditCardNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                cardNumberValidator.setText("This field is required!");
                cardNumberValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else if (newValue.length() != 16) {
                cardNumberValidator.setText("Card number consists of 16 digits.");
                cardNumberValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else if (!isNumeric(newValue)) {
                cardNumberValidator.setText("Card number constains only digits.");
                cardNumberValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else {
                cardNumberValidator.setVisible(false);
                if(chosenBank != null && !cvcValidator.isVisible() && !expiryDateValidator.isVisible())
                    addAccBtn.setDisable(false);
            }
        });
    }

    private void validateCvc() {
        cvc.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() == 0) {
                cvcValidator.setText("This field is required!");
                cvcValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else if (newValue.length() != 3) {
                cvcValidator.setText("CVC consists of 3 digits.");
                cvcValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else if (!isNumeric(newValue)) {
                cvcValidator.setText("CVC contains only digits.");
                cvcValidator.setVisible(true);
                addAccBtn.setDisable(true);
            } else {
                cvcValidator.setVisible(false);
                if(chosenBank != null && !cardNumberValidator.isVisible() && !expiryDateValidator.isVisible())
                    addAccBtn.setDisable(false);
            }
        });
    }

    private void validateExpiryDate() {
        if(chosenDate.isBefore(LocalDate.now())) {
            expiryDateValidator.setText("Your card is not valid anymore!");
            expiryDateValidator.setVisible(true);
            addAccBtn.setDisable(true);
        } else {
            expiryDateValidator.setVisible(false);
            addAccBtn.setDisable(false);
        }
    }

    private boolean isNumeric(String string) {
        return string.matches("\\d+");
    }

    public void convertToObservableList (JsonArray dbBanks) {
        bankList.clear();
        for(int i = 0; i < dbBanks.size(); i++) {
            JsonObject bank = dbBanks.getJsonObject(i);
            bankList.add(new Bank((long) bank.getInt("id"), bank.getString("bankName")));
            comboBoxBanks.setItems(bankList);
        }
    }
}
