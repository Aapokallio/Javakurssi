package tamk.ohsyte;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Today {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("No matching file found in user/today");
            return;
        }
        String homeDir = System.getProperty("user.home");
        final String fileName = homeDir + File.separator + "today" + File.separator + args[0];

        EventProvider provider = new CSVEventProvider(fileName);
        final MonthDay monthDay = MonthDay.of(2, 11);

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File not found: " + fileName);
            return;
        }
        // Get events for given day, any year, any category, newest first
        List<Event> events = provider.getEventsOfDate(monthDay);
        Collections.sort(events);
        Collections.reverse(events);

        for (Event event : events) {
            System.out.println(event);
        }
    }
}