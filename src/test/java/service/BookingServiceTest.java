package service;

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

public class BookingServiceTest extends TestCase {
    BookingRepository bookingRepository = new BookingRepository();
    BookingService bookingService = new BookingService(bookingRepository);

    BranchRepository branchRepository = new BranchRepository();
    BranchService branchService = new BranchService(bookingService, branchRepository);

    public void testFindBookingForVehicleTypeInTimeRange_LowerRange_Success()
            throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        Vehicle vehicle = new Vehicle("Van1", 100D, VehicleType.VAN);
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN));
        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        bookingService.addBooking(branch, vehicle, 1, 4, 500D);

        //Act
        List<Booking> result = bookingService.findBookingForVehicleTypeInTimeRange(branch, VehicleType.VAN, 0, 3);

        //Assert
        Assert.assertEquals(1,result.size());
        Assert.assertEquals("branch1",result.get(0).getBranch().getName());
    }

    public void testFindBookingForVehicleTypeInTimeRange_UpperRange_Success()
            throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        Vehicle vehicle = new Vehicle("Van1", 100D, VehicleType.VAN);
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN));
        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        bookingService.addBooking(branch, vehicle, 1, 4, 500D);

        //Act
        List<Booking> result = bookingService.findBookingForVehicleTypeInTimeRange(branch, VehicleType.VAN, 4, 7);

        //Assert
        Assert.assertEquals(1,result.size());
        Assert.assertEquals("branch1",result.get(0).getBranch().getName());
    }

    public void testFindBookingForVehicleTypeInTimeRange_BoundaryCheck()
            throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        Vehicle vehicle = new Vehicle("Van1", 100D, VehicleType.VAN);
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN));
        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        bookingService.addBooking(branch, vehicle, 1, 4, 500D);

        //Act
        List<Booking> result = bookingService.findBookingForVehicleTypeInTimeRange(branch, VehicleType.VAN, 0, 3);

        //Assert
        Assert.assertEquals(1,result.size());
        Assert.assertEquals("branch1",result.get(0).getBranch().getName());
    }

    public void testFindAvailableVehiclesBetween_Success()
            throws NotFoundException, InvalidOperationException, AlreadyExistsException {
        //Arrange
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        Vehicle vehicle1 = new Vehicle("Van1", 100D, VehicleType.VAN);
        Vehicle vehicle2 = new Vehicle("Car1", 100D, VehicleType.CAR);
        Vehicle vehicle3 = new Vehicle("Car2", 100D, VehicleType.CAR);

        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);
        bookingService.addBooking(branch, vehicle1, 3, 5, 500D);
        bookingService.addBooking(branch, vehicle2, 1, 3, 100D);
        bookingService.addBooking(branch, vehicle3, 5, 6, 100D);

        //Act
        List<String> result = bookingService.findAvailableVehiclesBetween(branch, 1, 4);

        //Assert
        Assert.assertEquals(1,result.size());
        Assert.assertEquals("Car2",result.get(0));
    }


    public void testFindAvailableVehiclesBetween_BoundaryCheck_Success()
            throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        Vehicle vehicle1 = new Vehicle("Van1", 100D, VehicleType.VAN);
        Vehicle vehicle2 = new Vehicle("Car1", 100D, VehicleType.CAR);
        Vehicle vehicle3 = new Vehicle("Car2", 100D, VehicleType.CAR);

        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);
        bookingService.addBooking(branch, vehicle1, 3, 5, 500D);
        bookingService.addBooking(branch, vehicle2, 1, 3, 100D);
        bookingService.addBooking(branch, vehicle3, 5, 6, 100D);

        //Act
        List<String> lowerResult = bookingService.findAvailableVehiclesBetween(branch, 0, 1);
        List<String> upperResult = bookingService.findAvailableVehiclesBetween(branch, 6, 7);

        //Assert
        Assert.assertEquals(3,lowerResult.size());
        Assert.assertEquals(3,upperResult.size());
    }

    public void testFindPercentageOfCarsBooked_Success()
            throws AlreadyExistsException, NotFoundException, InvalidOperationException {
        //Arrange
        Branch branch = branchService.createBranch("branch1", Arrays.asList(VehicleType.VAN, VehicleType.CAR));

        Vehicle vehicle1 = new Vehicle("Van1", 100D, VehicleType.VAN);
        Vehicle vehicle2 = new Vehicle("Car1", 100D, VehicleType.CAR);

        branchService.addVehicle("branch1", "Van1", VehicleType.VAN, 100D);
        branchService.addVehicle("branch1", "Car1", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car2", VehicleType.CAR, 100D);
        branchService.addVehicle("branch1", "Car3", VehicleType.CAR, 100D);
        bookingService.addBooking(branch, vehicle1, 3, 5, 500D);
        bookingService.addBooking(branch, vehicle2, 1, 3, 100D);

        //Act
        Double percentageOfCarsBooked = bookingService.findPercentageOfCarsBooked(branch, 0, 1);

        //Assert
        Assert.assertEquals(Double.valueOf(33.333333333333336),percentageOfCarsBooked);
    }
}