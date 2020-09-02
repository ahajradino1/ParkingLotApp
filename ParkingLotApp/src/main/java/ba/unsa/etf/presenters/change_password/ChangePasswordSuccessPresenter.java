package ba.unsa.etf.presenters.change_password;

import com.gluonhq.charm.glisten.application.MobileApplication;

import static ba.unsa.etf.GluonApplication.HOMEPAGE_VIEW;

public class ChangePasswordSuccessPresenter {
    public void backToHomePage() {
        MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);
    }
}
