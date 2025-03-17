package tamk.ohsyte.commands;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import tamk.ohsyte.EventManager;
import tamk.ohsyte.Today;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.providers.CSVEventProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;

@Command(
        name = "addevent",
        description = "Add a new event to the event provider"
)
public class AddEvent implements Callable<Integer> {
    @Option(names = "--date", required = true, description = "Event date (YYYY-MM-DD or --MM-DD)")
    private String date;

    @Option(names = "--category", required = true, description = "Event category (primary/secondary)")
    private String category;

    @Option(names = "--description", required = true, description = "Event description")
    private String description;

    @Option(names = "--provider", description = "Event provider identifier")
    private String providerId = "standard";

    @Override
    public Integer call() {
        try {
            boolean isAnnual = date.startsWith("--");
            String csvLine;

            if (isAnnual) {
                String monthDayStr = date.substring(2);
                MonthDay monthDay = MonthDay.parse(monthDayStr, DateTimeFormatter.ofPattern("MM-dd"));
                csvLine = String.format("--%s,%s,%s", monthDayStr, description, category);
            } else {
                LocalDate fullDate = LocalDate.parse(date);
                csvLine = String.format("%s,%s,%s", date, description, category);
            }

            Category.parse(category);

            try (FileWriter fw = new FileWriter(Today.CONFIG_PATH.toString(), true)) {
                if (new File(Today.CONFIG_PATH.toString()).length() == 0) {
                    fw.write("date,description,category\n");
                }
                fw.write(csvLine + "\n");
            }

            System.out.println("Event added successfully");
            return 0;

        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + e.getMessage());
            return 1;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid category format: " + e.getMessage());
            return 1;
        } catch (IOException e) {
            System.err.println("Error writing to events file: " + e.getMessage());
            return 1;
        }
    }
}