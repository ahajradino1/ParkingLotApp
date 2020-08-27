package ba.unsa.etf.models;

public class ParkingLot {
    private String code;
    private String address;
    private int freeSpots;
    private double costPerHour;
    private double maxStopTime;

    public ParkingLot(String code, int freeSpots) {
        this.code = code;
        this.freeSpots = freeSpots;
    }

    public ParkingLot(String code, String address, int freeSpots, double costPerHour, double maxStopTime) {
        this.code = code;
        this.address = address;
        this.freeSpots = freeSpots;
        this.costPerHour = costPerHour;
        this.maxStopTime = maxStopTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public double getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(double costPerHour) {
        this.costPerHour = costPerHour;
    }

    public double getMaxStopTime() {
        return maxStopTime;
    }

    public void setMaxStopTime(double maxStopTime) {
        this.maxStopTime = maxStopTime;
    }

    @Override
    public String toString() {
        return code;
    }
}
