package service;

import exception.AlreadyBookedException;
import exception.AlreadyExistsException;
import exception.InvalidOperationException;
import exception.NotFoundException;
import junit.framework.TestCase;
import model.Booking;
import model.Branch;
import model.vehicles.Vehicle;
import model.vehicles.VehicleType;
import org.junit.Assert;
import repository.BookingRepository;
import repository.BranchRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BranchServiceTest extends TestCase {

    BookingRepository bookingRepository = new BookingRepository();
    BookingService bookingService = new BookingService(bookingRepository);

    BranchRepository branchRepository = new BranchRepository();
    BranchService branchService = new BranchService(bookingService, branchRepository);



    public void testCreateBranch_Success() throws AlreadyExistsException {
        //Arrange and act
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.BIKE));
        //Assert
        Assert.assertEquals(branch.getName(), "branch1");
    }


    public void testAddVehicle_Success() throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.CAR));
        //Act
        Vehicle vehicle = branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        //Assert
        Assert.assertEquals(vehicle.getName(), "Car1");
    }


    public void testAddVehicle_InvalidType() throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN));
        //Act and assert
        assertThrows(InvalidOperationException.class, () ->
                branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D));
    }

    public void testBookVehicle_Success()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);

        //Act
        Booking booking = branchService.bookVehicle("branch1", "CAR", 1, 3);

        //Assert
        Assert.assertEquals(booking.getBookingCost(),Double.valueOf(200));
    }

    public void testBookVehicle_InvalidSlot()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));
        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);

        //Act and assert
        assertThrows(InvalidOperationException.class, () ->
                branchService.bookVehicle("branch1", "CAR", 3, 1));

    }

    public void testBookVehicle_checkPriceSurge()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));
        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car3", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car4", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car5", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car6", VehicleType.CAR, 100D);

        branchService.bookVehicle("branch1", "CAR", 1, 3);
        branchService.bookVehicle("branch1", "CAR", 1, 3);
        branchService.bookVehicle("branch1", "CAR", 1, 3);
        branchService.bookVehicle("branch1", "CAR", 1, 3);
        branchService.bookVehicle("branch1", "CAR", 1, 3);

        //Act
        Booking booking = branchService.bookVehicle("branch1", "CAR", 1, 3);

        //Assert
        Assert.assertEquals(booking.getBookingCost(), Double.valueOf(200*1.1));

    }

    public void testBookVehicle_vehiclesExhausted()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);


        branchService.bookVehicle("branch1", "CAR", 1, 3);
        branchService.bookVehicle("branch1", "CAR", 1, 3);


        //Act and assert
        assertThrows(AlreadyBookedException.class, () ->
                branchService.bookVehicle("branch1", "CAR", 1, 3));

    }


    public void testFindAvailableVehicles_Success()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 200D);


        branchService.bookVehicle("branch1", "CAR", 1, 3);


        //Act
        List<String> result = branchService.findAvailableVehicles("branch1", 1, 3);

        //Assert
        Assert.assertEquals(result.size(), 1);
        //More expensive vehicle remains
        Assert.assertEquals(result.get(0), "Car2");
    }

    public void testFindAvailableVehicles_InvalidSlot()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException, AlreadyBookedException {
        //Arrange
        branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 200D);


        branchService.bookVehicle("branch1", "CAR", 1, 3);


        //Act and assert
        assertThrows(InvalidOperationException.class, () ->
                branchService.bookVehicle("branch1", "CAR", 3, 1));
    }
}