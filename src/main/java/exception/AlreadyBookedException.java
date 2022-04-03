package exception;

public class AlreadyBookedException extends Exception {

    public AlreadyBookedException(String branchName, String vehicleType, Integer startTime, Integer endTime) {
        super(String.format("All Vehicles of type %s have already been booked for branch %s" +
                "between startTime %s and endTime : %s. Please choose a different slot."
                , vehicleType, branchName, startTime, endTime));
    }
}
