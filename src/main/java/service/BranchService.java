package service;

import exception.AlreadyBookedException;
import exception.AlreadyExistsException;
import exception.InvalidOperationException;
import exception.NotFoundException;
import model.Booking;
import model.Branch;
import model.City;
import model.vehicles.Vehicle;
import model.vehicles.VehicleType;
import repository.BranchRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static global.Constants.*;

public class BranchService {
    BookingService bookingService;
    BranchRepository branchRepository;


    public BranchService(BookingService bookingService, BranchRepository branchRepository) {
        this.bookingService = bookingService;
        this.branchRepository = branchRepository;
    }

    public Branch createBranch(String name, List<VehicleType> vehicleTypes) throws AlreadyExistsException {
        Branch branch = new Branch(name, new City("Bangalore"), vehicleTypes);
        Optional<Branch> branchByName = branchRepository.findBranchByName(name);
        if(branchByName.isPresent()) {
            throw new AlreadyExistsException(String.format(BRANCH_ALREADY_EXISTS, name));
        }
        return branchRepository.saveBranch(branch);
    }



    public Vehicle addVehicle(String branchName, String vehicleName, VehicleType type, Double price)
            throws NotFoundException, InvalidOperationException {
        Branch branch = branchRepository.findBranchByName(branchName)
                .orElseThrow(() ->
                        new NotFoundException(String.format(BRANCH_NOT_FOUND, branchName)));

        if(!branch.getVehicleTypes().contains(type)) {
            throw new InvalidOperationException(String.format(VEHICLE_NOT_SUPPORTED, branchName, type));
        }

        Vehicle vehicle = new Vehicle(vehicleName, price, type);
        Set<Vehicle> vehicleSet = branch.getVehicleSet();
        vehicleSet.add(vehicle);
        return vehicle;

    }



    //If 2 vehicles of same type available, will book the one with cheaper cost
    public Booking bookVehicle(String branchName, String vehicleType, Integer startTime, Integer endTime)
            throws NotFoundException, AlreadyBookedException, InvalidOperationException {
        if(startTime >= endTime) {
            throw new InvalidOperationException(INVALID_SLOT);
        }
        Branch branch = branchRepository.findBranchByName(branchName)
                .orElseThrow(() -> new NotFoundException(String.format(BRANCH_NOT_FOUND, branchName)));

        Vehicle vehicle = findFirstAvailableVehicle(branch, VehicleType.valueOf(vehicleType), startTime, endTime)
                .orElseThrow(() -> new NotFoundException(String.format(VEHICLE_NOT_FOUND, vehicleType, branchName)));
        Integer timeDuration = endTime - startTime;
        Double price = bookingService.findPercentageOfCarsBooked(branch, startTime, endTime) > 80.0D
                ? vehicle.getPrice()*1.1D * (timeDuration)
                : vehicle.getPrice() * (timeDuration);

        Booking booking = bookingService.addBooking(branch, vehicle, startTime, endTime, price);
        return booking;
    }



    public Optional<Vehicle> findFirstAvailableVehicle(Branch branch, VehicleType vehicleType, Integer startTime, Integer endTime) throws AlreadyBookedException {
        List<Vehicle> bookedVehicles = bookingService.findBookingForVehicleTypeInTimeRange(branch, vehicleType, startTime, endTime)
                .stream()
                .map(booking -> booking.getVehicle())
                .collect(Collectors.toList());

        List<Vehicle> allVehicles = branch.getVehicleSet()
                .stream()
                .filter(vehicle -> vehicle.getType() == vehicleType)
                .collect(Collectors.toList());

        if (bookedVehicles.size() >= allVehicles.size()) {
            throw new AlreadyBookedException(branch.getName(), vehicleType.toString(), startTime, endTime);
        }

        return allVehicles
                .stream().filter(vehicle -> !bookedVehicles.contains(vehicle))
                .sorted(Comparator.comparing(Vehicle::getPrice))
                .findFirst();
    }



    public List<String> findAvailableVehicles(String branchName, Integer startTime, Integer endTime)
            throws NotFoundException, InvalidOperationException {
        if(startTime >= endTime) {
            throw new InvalidOperationException(INVALID_SLOT);
        }
        Branch branch = branchRepository.findBranchByName(branchName)
                .orElseThrow(() -> new NotFoundException(String.format(BRANCH_NOT_FOUND, branchName)));
        return bookingService.findAvailableVehiclesBetween(branch, startTime, endTime);
    }
}
