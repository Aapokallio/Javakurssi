package tamk.ohsyte.commands;

import picocli.CommandLine;
import tamk.ohsyte.EventManager;
import tamk.ohsyte.providers.EventProvider;
import tamk.ohsyte.providers.SQLiteEventProvider;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@CommandLine.Command(name = "add", description = "Add a new event to the database")
public class AddEvent implements Runnable {
    @CommandLine.Option(names = {"-d", "--date"}, description = "Event date (YYYY-MM-DD)", required = true)
    private String date;

    @CommandLine.Option(names = {"-c", "--category"}, description = "Category name (xxx/yyy)", required = true)
    private String category;

    @CommandLine.Option(names = {"-t", "--text"}, description = "Event description", required = true)
    private String description;

    @CommandLine.Option(names = {"-p", "--provider"}, description = "Provider identifier", defaultValue = "sqlite")
    private String providerId;

    @Override
    public void run() {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format. Use YYYY-MM-DD");
            return;
        }

        EventManager manager = EventManager.getInstance();
        EventProvider provider = manager.getEventProvider(providerId);

        if (provider instanceof SQLiteEventProvider sqlProvider) {
            int categoryId = sqlProvider.ensureCategory(category);
            if (categoryId != -1) {
                sqlProvider.addEvent(date, description, categoryId);
            } else {
                System.err.println("Failed to process category");
            }
        } else {
            System.err.println("Provider not found or not SQLite provider");
        }
    }
}