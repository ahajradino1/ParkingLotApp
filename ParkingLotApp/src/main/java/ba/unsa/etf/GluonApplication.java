package ba.unsa.etf;

import ba.unsa.etf.views.*;
import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.BottomNavigation;
import com.gluonhq.charm.glisten.control.BottomNavigationButton;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class GluonApplication extends MobileApplication {
    public static final String PRIMARY_VIEW = "Primary Vire";
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String LOGIN_VIEW = HOME_VIEW;
    public static final String SIGNUP_VIEW = "Signup View";
    public static final String SIGNUP_SUCCESS_VIEW = "Signup Success View";
    public static final String HOMEPAGE_VIEW = "Home Page View";
    public static final String TICKETS_VIEW = "Tickets View";
    public static final String ACCOUNT_VIEW = "Account View";
    public static final String BANK_ACCOUNTS_VIEW = "Bank Accounts View";
    public static final String REGISTRATION_PLATES_VIEW = "Registration Plates View";
    public static final String LOGOUT_VIEW = "Logout view";
    public static final String ADD_BANK_ACCOUNT_VIEW = "Add Bank Account View";
    public static final String ADD_BANK_ACC_SUCCESS_VIEW = "Successfully Added Bank Account View";
    public static final String ADD_REG_PLATE_VIEW = "Add Registration Plate View";
    public static final String ADD_REG_PLATE_SUCCESS_VIEW = "Successfully Added Registration Plate View";
    public static final String PAYMENT_SUCCESS_VIEW = "Successful Payment View";
    public static final String ALL_TICKETS_VIEW = "All Tickets View";
    public static final String ACTIVE_TICKETS_VIEW = "Active Tickets View";

    public static NavigationDrawer menu = new NavigationDrawer();
    public static BottomNavigation bottomNavigation = new BottomNavigation();


    @Override
    public void init() {
        addViewFactory(LOGIN_VIEW, () -> new LoginView().getView());
        addViewFactory(SIGNUP_VIEW, () -> new SignupView().getView());
        addViewFactory(SIGNUP_SUCCESS_VIEW, () -> new SignupSuccessView().getView());
        addViewFactory(HOMEPAGE_VIEW, () -> new HomeView().getView());
        addViewFactory(TICKETS_VIEW, () -> new TicketsView().getView());
        addViewFactory(ACCOUNT_VIEW, () -> new AccountView().getView());
        addViewFactory(BANK_ACCOUNTS_VIEW, () -> new BankAccountsView().getView());
        addViewFactory(REGISTRATION_PLATES_VIEW, () -> new RegistrationPlateView().getView());
        addViewFactory(LOGOUT_VIEW, () -> new LogoutView().getView());
        addViewFactory(ADD_BANK_ACCOUNT_VIEW, () -> new AddBankAccountView().getView());
        addViewFactory(ADD_BANK_ACC_SUCCESS_VIEW, () -> new AddBankAccSuccessView().getView());
        addViewFactory(ADD_REG_PLATE_VIEW, () -> new AddRegistrationPlateView().getView());
        addViewFactory(ADD_REG_PLATE_SUCCESS_VIEW, () -> new AddRegPlateSuccessView().getView());
        addViewFactory(PAYMENT_SUCCESS_VIEW, () -> new PaymentSuccessView().getView());

        //addViewFactory(PRIMARY_VIEW, () -> new PrimaryView().getView());
        //addViewFactory(SECONDARY_VIEW, () -> new SecondaryView().getView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.RED.assignTo(scene);
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        createDrawer();
        createBottomNavigation();
    }


    public static void createDrawer() {
        //todo promijeniti ikonu - neka bude ista kao za app
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
        menu.setHeader(header);

        //todo osmisliti sta ce sve biti u ovom meniju -  za sad su one defaultne 3 opcije
        final NavigationDrawer.Item homeItem = new NavigationDrawer.ViewItem("Home", MaterialDesignIcon.HOME.graphic(), HOMEPAGE_VIEW);
        final NavigationDrawer.Item receiptsItem = new NavigationDrawer.ViewItem("Tickets", MaterialDesignIcon.RECEIPT.graphic(), TICKETS_VIEW);
        final NavigationDrawer.Item accountItem = new NavigationDrawer.ViewItem("Account", MaterialDesignIcon.PERSON.graphic(), ACCOUNT_VIEW);
        final NavigationDrawer.Item bankAccountsItem = new NavigationDrawer.ViewItem("Bank accounts", MaterialDesignIcon.CREDIT_CARD.graphic(), BANK_ACCOUNTS_VIEW);
        final NavigationDrawer.Item registrationPlatesItem = new NavigationDrawer.ViewItem("Registration plates", MaterialDesignIcon.DIRECTIONS_CAR.graphic(), REGISTRATION_PLATES_VIEW);
        final NavigationDrawer.Item logoutItem = new NavigationDrawer.ViewItem("Logout", MaterialDesignIcon.EXIT_TO_APP.graphic(), LOGOUT_VIEW);
        //final NavigationDrawer.Item primaryItem = new NavigationDrawer.ViewItem("Primary", MaterialDesignIcon.HOME.graphic(), PRIMARY_VIEW, ViewStackPolicy.SKIP);
        //final NavigationDrawer.Item secondaryItem = new NavigationDrawer.ViewItem("Secondary", MaterialDesignIcon.DASHBOARD.graphic(), SECONDARY_VIEW);
        menu.getItems().addAll(homeItem, accountItem, receiptsItem, bankAccountsItem, registrationPlatesItem, logoutItem);

        //todo ovo na kraju treba da obrisem jer app nije predvidjena za desktop
        if (Platform.isDesktop()) {
            final NavigationDrawer.Item quitItem = new NavigationDrawer.Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            menu.getItems().add(quitItem);
        }
    }

    public static void createBottomNavigation() {
        BottomNavigationButton btn1 = new BottomNavigationButton("Find parking lot", MaterialDesignIcon.SEARCH.graphic(), e -> System.out.println("Nadji parking"));
        BottomNavigationButton btn2 = new BottomNavigationButton("Tickets", MaterialDesignIcon.CREDIT_CARD.graphic(), e -> System.out.println("Aktivni parkinzi"));
        bottomNavigation.getActionItems().addAll(btn1, btn2);
    }

}
