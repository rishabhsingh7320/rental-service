package repository;

import global.Globals;
import model.Booking;
import model.Branch;
import model.vehicles.VehicleType;

import java.util.List;
import java.util.stream.Collectors;

public class BookingRepository {

    public List<Booking> findAll() {
        return Globals.bookingList;
    }

    public List<Booking> findAllByBranchAndVehicleType(Branch branch, VehicleType vehicleType) {
        return Globals.bookingList
                .stream()
                .filter(booking -> booking.getBranch().equals(branch))
                .filter(booking -> booking.getVehicle().getType().equals(vehicleType))
                .collect(Collectors.toList());
    }

    public List<Booking> findAllByBranch(Branch branch) {
        return Globals.bookingList
                .stream()
                .filter(booking -> booking.getBranch().equals(branch))
                .collect(Collectors.toList());
    }

    public Booking saveBooking(Booking booking) {
        Globals.bookingList.add(booking);
        return booking;
    }
}
