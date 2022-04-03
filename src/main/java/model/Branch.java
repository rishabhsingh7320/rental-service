package model;

import model.vehicles.Vehicle;
import model.vehicles.VehicleType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Branch {

    private String name;

    private City city;

    Set<Vehicle> vehicleSet;

    public Branch(String name, City city, List<VehicleType> vehicleTypes) {
        this.name = name;
        this.city = city;
        this.vehicleTypes = vehicleTypes;
        this.vehicleSet = new HashSet<>();
    }

    public Branch(String name, City city) {
        this.name = name;
        this.city = city;
    }

    List<VehicleType> vehicleTypes;



    public Branch(String name, City city, Set<Vehicle> vehicleSet, List<VehicleType> vehicleTypes) {
        this.name = name;
        this.city = city;
        this.vehicleSet = vehicleSet;
        this.vehicleTypes = vehicleTypes;
    }


    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }






    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        Branch branchObj = (Branch) obj;
        return this.name == branchObj.name;
    }

    public Set<Vehicle> getVehicleSet() {
        return vehicleSet;
    }

    public void setVehicleSet(Set<Vehicle> vehicleSet) {
        this.vehicleSet = vehicleSet;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "name='" + name + '\'' +
                ", city=" + city +
                ", vehicleSet=" + vehicleSet +
                ", vehicleTypes=" + vehicleTypes +
                '}';
    }
}
