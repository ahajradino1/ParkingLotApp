package ba.unsa.etf.models;

public class RegistrationPlate {
    private Long id;
    private String registrationNumber;

    public RegistrationPlate(Long id, String registrationNumber) {
        this.id = id;
        this.registrationNumber = registrationNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return registrationNumber;
    }
}
