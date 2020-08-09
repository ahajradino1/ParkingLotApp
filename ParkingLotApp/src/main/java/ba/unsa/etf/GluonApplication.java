package ba.unsa.etf;

import ba.unsa.etf.views.*;
import com.gluonhq.charm.down.Platform;
import com.gluonhq.charm.down.Services;
import com.gluonhq.charm.down.plugins.LifecycleService;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
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
    public static NavigationDrawer menu = new NavigationDrawer();


    @Override
    public void init() {
        addViewFactory(LOGIN_VIEW, () -> new LoginView().getView());
        addViewFactory(SIGNUP_VIEW, () -> new SignupView().getView());
        addViewFactory(SIGNUP_SUCCESS_VIEW, () -> new SignupSuccessView().getView());
        addViewFactory(HOMEPAGE_VIEW, () -> new HomeView().getView());

        addViewFactory(PRIMARY_VIEW, () -> new PrimaryView().getView());
        addViewFactory(SECONDARY_VIEW, () -> new SecondaryView().getView());
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.RED.assignTo(scene);
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
        createDrawer();
    }


    public static void createDrawer() {
        //todo promijeniti ikonu - neka bude ista kao za app
        NavigationDrawer.Header header = new NavigationDrawer.Header("Gluon Mobile",
                "Multi View Project",
                new Avatar(21, new Image(DrawerManager.class.getResourceAsStream("/icon.png"))));
        menu.setHeader(header);

        //todo osmisliti sta ce sve biti u ovom meniju -  za sad su one defaultne 3 opcije
        final NavigationDrawer.Item primaryItem = new NavigationDrawer.ViewItem("Primary", MaterialDesignIcon.HOME.graphic(), PRIMARY_VIEW, ViewStackPolicy.SKIP);
        final NavigationDrawer.Item secondaryItem = new NavigationDrawer.ViewItem("Secondary", MaterialDesignIcon.DASHBOARD.graphic(), SECONDARY_VIEW);
        menu.getItems().addAll(primaryItem, secondaryItem);

        //todo ovo na kraju mogu da obrisem jer app nije predvidjena za desktop
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

}
