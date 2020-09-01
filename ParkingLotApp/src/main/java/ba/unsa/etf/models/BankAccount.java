package ba.unsa.etf.models;

public class BankAccount {
    private Long id;
    private String accountOwner;
    private String bankName;
    private String expiryDate;
    private String cardNumber;

    public BankAccount(Long id, String accountOwner, String bankName, String expiryDate, String cardNumber) {
        this.id = id;
        this.accountOwner = accountOwner;
        this.bankName = bankName;
        this.expiryDate = expiryDate;
        this.cardNumber = cardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return cardNumber;
    }
}
