package tamk.ohsyte.providers.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import tamk.ohsyte.datamodel.Category;
import tamk.ohsyte.datamodel.Event;
import tamk.ohsyte.providers.EventProvider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;

public class WebEventProvider implements EventProvider {
    private final URI uri;
    private MonthDay monthDay;
    private List<Event> events;
    private String identifier;

    public WebEventProvider(java.net.URI uri, String identifier) {
        this.uri = uri;
        this.identifier = identifier;
        this.monthDay = monthDay.now();
        this.events = List.of();
    }

    private List<Event> foundEvents() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String date = String.format("%02d-%02d", monthDay.getMonthValue(), monthDay.getDayOfMonth());
            String eventsParameters = String.format("?date=%s", date);
            System.out.println(eventsParameters);
            URI requestUri = URI.create(uri.toString() + eventsParameters);
            System.out.println(requestUri);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(requestUri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response body: " + response.body());
            if (response.statusCode() != 200) {
                System.err.printf("HTTP response: %d%n", response.statusCode());
                return List.of();
            }

            SimpleModule module = new SimpleModule("EventDeserializer");
            module.addDeserializer(Event.class, new EventDeserializer());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(module);

            JavaType customClassCollection = mapper.getTypeFactory().constructCollectionType(List.class, Event.class);
            return mapper.readValue(response.body(), customClassCollection);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error fetching events: " + e.getMessage());
            return List.of();
        }
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
        this.events = foundEvents();
    }

    @Override
    public List<Event> getEvents() {
        return this.events;
    }

    @Override
    public List<Event> getEventsOfCategory(Category category) {
        List<Event> matchingEvents = new ArrayList<>();
        for (Event event : this.events) {
            if (event.getCategory().equals(category)) {
                matchingEvents.add(event);
            }
        }
        return matchingEvents;
    }

    @Override
    public List<Event> getEventsOfDate(MonthDay monthDay) {
        if (this.monthDay.getMonthValue() == monthDay.getMonthValue()
                && this.monthDay.getDayOfMonth() == monthDay.getDayOfMonth()) {
            return this.events;
        }
        return List.of();
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }
}
