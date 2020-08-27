package ba.unsa.etf.presenters;

import com.gluonhq.charm.glisten.application.MobileApplication;
import static ba.unsa.etf.GluonApplication.LOGIN_VIEW;

public class SignupSuccessPresenter {

    public void backToLogin() {
        MobileApplication.getInstance().switchView(LOGIN_VIEW);
    }
}
