package ba.unsa.etf.presenters.bank_accounts;


import com.gluonhq.charm.glisten.application.MobileApplication;
import static ba.unsa.etf.GluonApplication.BANK_ACCOUNTS_VIEW;
import static ba.unsa.etf.GluonApplication.HOMEPAGE_VIEW;

public class AddBankAccSuccessPresenter {

    public void backToAllAccounts() {
        MobileApplication.getInstance().switchView(BANK_ACCOUNTS_VIEW);
    }

    public void backToAllParkings() {
        MobileApplication.getInstance().switchView(HOMEPAGE_VIEW);
    }
}
