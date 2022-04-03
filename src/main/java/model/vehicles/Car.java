package model.vehicles;

public class Car extends Vehicle {
    public Car(String name, Double price) {
        super(name, price, VehicleType.CAR);
    }
}
