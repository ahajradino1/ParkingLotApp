package ba.unsa.etf.models;

public class ParkingLot {
    private Long id;
    private String zoneCode;
    private String streetAddress;
    private String municipality;
    private double costPerHour;

    public ParkingLot(Long id, String zoneCode, String streetAddress, String municipality, double costPerHour) {
        this.id = id;
        this.zoneCode = zoneCode;
        this.streetAddress = streetAddress;
        this.municipality = municipality;
        this.costPerHour = costPerHour;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public double getCostPerHour() {
        return costPerHour;
    }

    public void setCostPerHour(double costPerHour) {
        this.costPerHour = costPerHour;
    }
}
