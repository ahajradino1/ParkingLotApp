package ba.unsa.etf.presenters;

import com.gluonhq.charm.glisten.application.MobileApplication;
import static ba.unsa.etf.GluonApplication.TICKETS_VIEW;

public class PaymentSuccessPresenter {
    public void backToTickets() {
        MobileApplication.getInstance().switchView(TICKETS_VIEW);
    }
}
