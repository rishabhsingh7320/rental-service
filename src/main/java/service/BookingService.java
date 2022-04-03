package service;

import model.Booking;
import model.Branch;
import model.vehicles.Vehicle;
import model.vehicles.VehicleType;
import repository.BookingRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingService {
    public BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> findBookingForVehicleTypeInTimeRange(Branch branch, VehicleType vehicleType, Integer startTime, Integer endTime) {
        return bookingRepository
                .findAllByBranchAndVehicleType(branch, vehicleType)
                .stream()
                .filter(booking -> (booking.getStartTime() >= startTime && booking.getStartTime() < endTime) ||
                        (booking.getEndTime() > startTime && booking.getEndTime() <= endTime) ||
                        (booking.getStartTime() <= startTime && booking.getEndTime() >= endTime))
                .collect(Collectors.toList());
    }

    public List<Booking> findBookingInTimeRange(Branch branch, Integer startTime, Integer endTime) {
        return bookingRepository.findAllByBranch(branch)
                .stream()
                .filter(booking -> (booking.getStartTime() >= startTime && booking.getStartTime() < endTime) ||
                        (booking.getEndTime() > startTime && booking.getEndTime() <= endTime) ||
                        (booking.getStartTime() <= startTime && booking.getEndTime() >= endTime))
                .collect(Collectors.toList());
    }

    public List<String> findAvailableVehiclesBetween(Branch branch, Integer startTime, Integer endTime) {
        List<Vehicle> bookedVehicles = findBookingInTimeRange(branch, startTime, endTime)
                .stream().map(booking -> booking.getVehicle()).collect(Collectors.toList());
        Set<Vehicle> allVehicles = branch.getVehicleSet();

        return allVehicles
                .stream()
                .filter(vehicle -> !bookedVehicles
                        .stream()
                        .anyMatch(bookedVehicle -> bookedVehicle.getName().equals(vehicle.getName())))
                .map(vehicle -> vehicle.getName())
                .collect(Collectors.toList());
    }


    public Double findPercentageOfCarsBooked(Branch branch, Integer startTime, Integer endTime) {
        Long carsBooked =
                bookingRepository.findAllByBranchAndVehicleType(branch, VehicleType.CAR)
                .stream()
                .filter(booking -> ((booking.getStartTime() >= startTime && booking.getStartTime() <= endTime)
                        || (booking.getEndTime() >= startTime && booking.getEndTime() <= endTime)))
                .count();

        Long totalCars = branch
                .getVehicleSet()
                .stream()
                .filter(vehicle -> vehicle.getType() == VehicleType.CAR)
                .count();
        return totalCars == 0 ? 0 : ((carsBooked*100D)/totalCars);

    }


    public Booking addBooking(Branch branch, Vehicle vehicle , Integer startTime, Integer endTime, Double price) {
        Booking booking = new Booking(branch, vehicle, startTime, endTime, price);
        return bookingRepository.saveBooking(booking);
    }

}
