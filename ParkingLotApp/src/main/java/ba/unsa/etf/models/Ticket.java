package ba.unsa.etf.models;

import java.util.Date;

public class Ticket {
    private String id;
    private String cardNumber;
    private String registrationNumber;
    private ParkingLot parkingLot;
    private Date startingTime;
    private Date endingTime;
    private Double price;

    public Ticket(String id, String cardNumber, String registrationNumber, ParkingLot parkingLot, Date startingTime, Date endingTime, Double price) {
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

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public Date getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
