package controller;

import model.Booking;
import model.vehicles.VehicleType;
import exception.AlreadyBookedException;
import exception.AlreadyExistsException;
import exception.InvalidOperationException;
import exception.NotFoundException;
import service.BranchService;

import java.util.List;
import java.util.stream.Collectors;

public class BranchController {
    BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    public String createBranch(String name, List<String> vehicleTypes) {
        List<VehicleType> vehicleTypeList = vehicleTypes.stream()
                .map(type -> VehicleType.valueOf(type))
                .collect(Collectors.toList());
        try {
            branchService.createBranch(name, vehicleTypeList);
            return Boolean.TRUE.toString();
        } catch (AlreadyExistsException ex) {
            //ex.printStackTrace();
            return Boolean.FALSE.toString();
        }
    }



    public String addVehicle(String branchName, String vehicleName, String vehicleType, Double price) {
        try {
            branchService.addVehicle(branchName, vehicleName, VehicleType.valueOf(vehicleType), price);
            return Boolean.TRUE.toString();
        } catch (NotFoundException | InvalidOperationException e ) {
            //e.printStackTrace();
            return Boolean.FALSE.toString();
        }
    }

    public String bookVehicle(String branchName, String vehicleType, Integer startTime, Integer endTime) {
        try {
            Booking booking = branchService.bookVehicle(branchName, vehicleType, startTime, endTime);
            return String.valueOf(booking.getBookingCost());
        } catch (NotFoundException | AlreadyBookedException | InvalidOperationException e) {
            //e.printStackTrace();
            return String.valueOf(-1);
        }
    }

    public String findAvailableVehicles(String branchName, Integer startTime, Integer endTime) {
        try {
            return branchService.findAvailableVehicles(branchName, startTime, endTime).toString();
        } catch (NotFoundException | InvalidOperationException e) {
            //e.printStackTrace();
            return "NULL";
        }
    }


}
