package tamk.ohsyte;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import tamk.ohsyte.commands.AddEvent;
import tamk.ohsyte.commands.ListEvents;
import tamk.ohsyte.commands.ListProviders;
import tamk.ohsyte.datamodel.*;
import tamk.ohsyte.providers.CSVEventProvider;
import tamk.ohsyte.providers.EventProvider;
import tamk.ohsyte.providers.SQLiteEventProvider;

@Command(name = "today", subcommands = { ListProviders.class, ListEvents.class, AddEvent.class }, description = "Shows events from history and annual observations")
public class Today {
    public Today() {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Construct a path to a file in the user's home directory,
        // in a subdirectory called ".today".
        String homeDirectory = System.getProperty("user.home");
        String configDirectory = ".today";
        Path csvPath = Paths.get(homeDirectory, configDirectory, "events.csv");
        //System.out.println("Path = " + path.toString());

        // Create the events file if it doesn't exist
        if (!Files.exists(csvPath)) {
            try {
                Files.createFile(csvPath);
            } catch (IOException e) {
                System.err.println("Unable to create events file");
                System.exit(1);
            }
        }

        String eventProviderId = "standard";

        // Add a CSV event provider that reads from the given file.
        manager.addEventProvider(
                new CSVEventProvider(csvPath.toString(), eventProviderId));

        // Add an SQLite database event provider.
        Path databasePath = Paths.get(homeDirectory, configDirectory, "events.sqlite3");
        EventProvider sqliteEventProvider = new SQLiteEventProvider(databasePath.toString());
        manager.addEventProvider(sqliteEventProvider);

        System.out.println("TEST SQLiteEventProvider");
        List<Event> sqliteEvents = sqliteEventProvider.getEventsOfDate(MonthDay.of(3, 24));
        System.out.printf("Got %d events from SQLiteEventProvider%n", sqliteEvents.size());
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Today()).execute(args);
        System.exit(exitCode);
    }
}
