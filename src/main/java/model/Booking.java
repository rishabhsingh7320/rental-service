package model;

import model.vehicles.Vehicle;

public class Booking {

    private Branch branch;

    private Vehicle vehicle;

    private Integer startTime;

    private Integer endTime;

    private Double bookingCost;



    public Booking(Branch branch, Vehicle vehicle, Integer startTime, Integer endTime, Double bookingCost) {
        this.branch = branch;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookingCost = bookingCost;
    }

    public Double getBookingCost() {
        return bookingCost;
    }

    public void setBookingCost(Double bookingCost) {
        this.bookingCost = bookingCost;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "branch=" + branch +
                ", vehicle=" + vehicle +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", bookingCost=" + bookingCost +
                '}';
    }
}
