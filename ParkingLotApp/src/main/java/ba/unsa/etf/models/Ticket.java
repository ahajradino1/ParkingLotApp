package ba.unsa.etf.models;


public class Ticket {
    private String id;
    private String cardNumber;
    private String registrationNumber;
    private ParkingLot parkingLot;
    private String startingTime;
    private String endingTime;
    private Double price;

    public Ticket(String id, String cardNumber, String registrationNumber, ParkingLot parkingLot, String startingTime, String endingTime, Double price) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.registrationNumber = registrationNumber;
        this.parkingLot = parkingLot;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(String endingTime) {
        this.endingTime = endingTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
