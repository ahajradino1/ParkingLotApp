package ba.unsa.etf.presenters;


import com.gluonhq.charm.glisten.application.MobileApplication;
import static ba.unsa.etf.GluonApplication.BANK_ACCOUNTS_VIEW;

public class AddBankAccSuccessPresenter {

    public void backToAllAccounts() {
        MobileApplication.getInstance().switchView(BANK_ACCOUNTS_VIEW);
    }
}
