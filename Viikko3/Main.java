import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {

        ArrayList<Event> events = new ArrayList<>();

        events.add(new Event(LocalDate.of(2024, 9, 16), "macOS 15 Sequoia released", new Category("apple", "macOS")));
        events.add(new Event(LocalDate.of(2023, 9, 26), "macOS 14 Sonoma released", new Category("apple", "macOS")));
        events.add(new Event(LocalDate.of(2022, 10, 24), "macOS 13 Ventura released", new Category("apple", "macOS")));
        events.add(new Event(LocalDate.of(2021, 10, 25), "macOS 12 Monterey released", new Category("apple", "macOS")));
        events.add(new Event(LocalDate.of(2020, 11, 12), "macOS 11 Big Sur released", new Category("apple", "macOS")));

        for (Event event : events) { // johdetaan lauseke released pidemmäksi ja haetaan viikonpäivä loppuun
            System.out.println(event.getDescription()
                    .replaceAll("\\s+released$", " was released on a ")
                    + event.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        }

        String[] eventNames = new String[events.size()];

        for (int i = 0; i < events.size(); i++) { //haetaan numeron ja released sanan väli joka eventistä lyödään listaan
            eventNames[i] = events.get(i).getDescription().replaceAll("^.*?\\d+\\s+", "").replaceAll("\\s+released$", "");
        }
        Arrays.sort(eventNames);
        System.out.println("In aplhabetical order: " + Arrays.toString(eventNames));

    }
}