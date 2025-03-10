package tamk.ohsyte;

import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.util.*;
import java.util.stream.Collectors;

public class Today {
    public static void main(String[] args) {
        // Gets the singleton manager. Later calls to getInstance
        // will return the same reference.
        EventManager manager = EventManager.getInstance();

        // Add a CSV event provider that reads from the given file.
        // Replace with a valid path to the events.csv file on your own computer!
        String fileName = "C:\\Users\\aapok\\Downloads\\events.csv";
        manager.addEventProvider(new CSVEventProvider(fileName));

        fileName = "C:\\Users\\aapok\\IdeaProjects\\vk7\\07\\singular-events.csv";
        manager.addEventProvider(new CSVEventProvider(fileName));

        MonthDay today = MonthDay.now();
        List<Event> allEvents = manager.getEventsOfDate(today);
        List<AnnualEvent> annualEvents = new ArrayList<>();
        List<SingularEvent> singularEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event instanceof AnnualEvent) {
                annualEvents.add((AnnualEvent) event);
            } else if (event instanceof SingularEvent) {
                singularEvents.add((SingularEvent) event);
            }
        }
        System.out.println(annualEvents.size());
        System.out.println(singularEvents.size());
        System.out.println("Today:");
        Collections.sort(annualEvents, new AnnualEventComparator());

        for (AnnualEvent a : annualEvents) {
            System.out.printf(
                    "- %s (%s) %n",
                    a.getDescription(),
                    a.getCategory());
        }
        //System.out.printf("%d events%n", annualEvents.size());

        System.out.println("\nToday in history:");
        Collections.sort(singularEvents, new SingularEventComparator());
        Collections.reverse(singularEvents);

        for (SingularEvent s : singularEvents) {
            int year = s.getDate().getYear();
            if (year < 2015) {
                continue;
            }

            System.out.printf(
                    "%d: %s (%s)%n",
                    year,
                    s.getDescription(),
                    s.getCategory());
        }
        //System.out.printf("%d events%n", singularEvents.size());
        //Testikeissit
        DateFilter dateFilterWithYear = new DateFilter(today, 2016);
        List<Event> dateFilteredEventsWithYear = manager.getFilteredEvents(dateFilterWithYear);
        DateFilter dateFilterWithoutYear = new DateFilter(today);
        List<Event> dateFilteredEventsWithoutYear = manager.getFilteredEvents(dateFilterWithoutYear);
        Category societyCategory = new Category("society");
        CategoryFilter societyCategoryFilter = new CategoryFilter(societyCategory);
        List<Event> categoryFilteredEvents = manager.getFilteredEvents(societyCategoryFilter);
        Category testCategory = new Category("test");
        DateCategoryFilter dateCategoryFilter = new DateCategoryFilter(MonthDay.of(5, 9), testCategory);
        List<Event> dateFilteredEventsWithCategory = manager.getFilteredEvents(dateCategoryFilter);

        System.out.println("All events size: " + manager.getAllEvents().size());
        System.out.println("Filtered by today's date with year 2016 size: " + dateFilteredEventsWithYear.size());
        System.out.println("Filtered by today's date without a year size: " + dateFilteredEventsWithoutYear.size());
        System.out.println("Filtered by category 'society' size: " + categoryFilteredEvents.size());
        System.out.println("Filtered by category 'test' and date 9.5. size: " + dateFilteredEventsWithCategory.size());

    }
}
