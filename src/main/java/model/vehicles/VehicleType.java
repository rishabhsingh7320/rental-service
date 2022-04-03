package model.vehicles;

public enum VehicleType {
    CAR("CAR"), VAN("VAN"), BIKE("BIKE"), BUS("BUS");

    private String value;

    VehicleType(String value) {
        this.value = value;
    }
}
