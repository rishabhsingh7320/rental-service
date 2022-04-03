import repository.BookingRepository;
import repository.BranchRepository;
import service.BookingService;
import controller.BranchController;
import service.BranchService;
import exception.InvalidOperationException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;

public class Driver {

    public static void main(String[] args) throws Throwable {


        //Dependency injection
        BookingRepository bookingRepository = new BookingRepository();
        BranchRepository branchRepository = new BranchRepository();

        BookingService bookingService = new BookingService(bookingRepository);
        BranchService branchService = new BranchService(bookingService, branchRepository);

        BranchController branchController = new BranchController(branchService);
        CommandFactory commandFactory = new CommandFactory(branchController);

        Map<String, Function<List<String>, String>> commands = commandFactory.buildCommands();


        //Reading from file input.txt
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
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
