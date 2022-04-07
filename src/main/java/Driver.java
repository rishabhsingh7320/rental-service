import repository.BookingRepository;
import repository.BranchRepository;
import service.BookingService;
import controller.BranchController;
import service.BranchService;
import exception.InvalidOperationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Driver {

    /**
     * Since we are not using sprong boot to run the project, we are injecting dependencies manually here.
     * This can me done by using autowired in a spring boot project
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        String filePath = "input.txt";
        //Fetch commands from factory pattern
        Map<String, Function<List<String>, String>> commands = getCommandFactory().buildCommands();
        //Reading from file input.txt
        executeCommandsFromFile(commands, filePath);

    }

    private static CommandFactory getCommandFactory() {
        BookingRepository bookingRepository = new BookingRepository();
        BranchRepository branchRepository = new BranchRepository();
        BookingService bookingService = new BookingService(bookingRepository);
        BranchService branchService = new BranchService(bookingService, branchRepository);
        BranchController branchController = new BranchController(branchService);
        CommandFactory commandFactory = new CommandFactory(branchController);
        return commandFactory;
    }

    private static void executeCommandsFromFile(Map<String, Function<List<String>, String>> commands, String filePath) throws IOException, InvalidOperationException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        Scanner sc = new Scanner(br);
        while ((line = br.readLine()) != null) {
            List<String> currentLine = Arrays.asList(line.split(" "));
            System.out.println(Optional.ofNullable(commands.get(currentLine.get(0)))
                    .orElseThrow(() -> new InvalidOperationException("INVALID COMMAND"))
                    .apply(currentLine));
        }
        sc.close();
    }
}
