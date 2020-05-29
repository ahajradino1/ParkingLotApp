package ba.unsa.etf;

import ba.unsa.etf.views.LoginView;
import ba.unsa.etf.views.PrimaryView;
import ba.unsa.etf.views.SecondaryView;
import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class GluonApplication extends MobileApplication {
    public static final String PRIMARY_VIEW = HOME_VIEW;
    public static final String SECONDARY_VIEW = "Secondary View";
    public static final String LOGIN_VIEW = HOME_VIEW;
    
    @Override
    public void init() {
        addViewFactory(LOGIN_VIEW, () -> new LoginView().getView());
        //addViewFactory(PRIMARY_VIEW, () -> new PrimaryView().getView());
//        addViewFactory(SECONDARY_VIEW, () -> new SecondaryView().getView());
//        DrawerManager.buildDrawer(this);
    }

    @Override
    public void postInit(Scene scene) {
        Swatch.RED.assignTo(scene);
        scene.getStylesheets().add(GluonApplication.class.getResource("style.css").toExternalForm());
    }
}
