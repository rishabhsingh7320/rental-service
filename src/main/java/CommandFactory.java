import controller.BranchController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CommandFactory {
    BranchController branchController;

    public static Map<String, Consumer<String>> commands = new HashMap<>();

    public CommandFactory(BranchController branchController) {
        this.branchController = branchController;
    }

    public Map<String, Function<List<String>, String>> buildCommands() {
        Map<String, Function<List<String>, String>> commands = new HashMap<>();
        commands.put("ADD_BRANCH", addBranch);
        commands.put("ADD_VEHICLE", addVehicle);
        commands.put("BOOK", bookVehicle);
        commands.put("DISPLAY_VEHICLES", displayVehicles);
        return commands;
    }

    public Function<List<String>, String> addBranch = (List<String> parameters) -> {
        String branchName = parameters.get(1);
        List<String> vehicleTypes = Arrays.asList(parameters.get(2).split(","));
        return branchController.createBranch(branchName, vehicleTypes);
    };

    public Function<List<String>, String> addVehicle = (List<String> parameters) -> {
        String branchName = parameters.get(1);
        String vehicleType = parameters.get(2);
        String vehicleName = parameters.get(3);
        Double price = Double.valueOf(parameters.get(4));
         return branchController.addVehicle(branchName, vehicleName, vehicleType, price);
    };


    public Function<List<String>, String> bookVehicle = (List<String> parameters) -> {
        String branchName = parameters.get(1);
        String vehicleType = parameters.get(2);
        Integer startTime = Integer.valueOf(parameters.get(3));
        Integer endTime = Integer.valueOf(parameters.get(4));
        return branchController.bookVehicle(branchName, vehicleType, startTime, endTime);
    };

    public Function<List<String>, String> displayVehicles = (List<String> parameters) -> {
        String branchName = parameters.get(1);
        Integer startTime = Integer.valueOf(parameters.get(2));
        Integer endTime = Integer.valueOf(parameters.get(3));
        return branchController.findAvailableVehicles(branchName, startTime, endTime);
    };


}
