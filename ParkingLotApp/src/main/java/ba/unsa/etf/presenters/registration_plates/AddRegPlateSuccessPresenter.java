package ba.unsa.etf.presenters.registration_plates;

import com.gluonhq.charm.glisten.application.MobileApplication;

import static ba.unsa.etf.GluonApplication.HOMEPAGE_VIEW;
import static ba.unsa.etf.GluonApplication.REGISTRATION_PLATES_VIEW;

public class AddRegPlateSuccessPresenter {

    public void backToAllPlates() {
        MobileApplication.getInstance().switchView(REGISTRATION_PLATES_VIEW);
    }

    public void backToAllParkings() {
        MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);
    }
}
