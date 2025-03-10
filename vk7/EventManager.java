package tamk.ohsyte;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages and queries the events available from event providers.
 */
public class EventManager {
    private static final EventManager INSTANCE = new EventManager();

    private final List<EventProvider> eventProviders;

    private EventManager() {
        this.eventProviders = new ArrayList<>();
    }

    /**
     * Gets the only instance of the event manager.
     *
     * @return the instance
     */
    public static EventManager getInstance() {
        return INSTANCE;
    }

    /**
     * Adds an event provider to the manager's list if it isn't
     * already there.
     *
     * @param provider the event provider to add
     * @return <code>true</code> if the provider was added, <code>false</code> otherwise
     */
    public boolean addEventProvider(EventProvider provider) {
       /* for (EventProvider ep : eventProviders) {
            if (ep.getIdentifier().equals(provider.getIdentifier())) {
                return false;
            }
        }*/
        this.eventProviders.add(provider);
        return true;
    }

    /**
     * Removes the specified event provider from the manager's list.
     *
     * @param providerId the identifier of the event provider to remove
     * @return <code>true</code> if the provider was removed, <code>false</code> if not
     */
    public boolean removeEventProvider(String providerId) {
        for (EventProvider ep : eventProviders) {
            if (ep.getIdentifier().equals(providerId)) {
                this.eventProviders.remove(ep);
                return true;
            }
        }
        return false;
    }

    /**
     * Get all the events available from all registered event providers.
     *
     * @return the list of all events
     */
    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEvents());
        }

        return events;
    }

    public List<Event> getEventsOfDate(MonthDay monthDay) {
        List<Event> events = new ArrayList<>();

        for (EventProvider provider : this.eventProviders) {
            events.addAll(provider.getEventsOfDate(monthDay));
        }

        return events;
    }

    /**
     * Gets the number of event providers for the manager.
     *
     * @return the number of event providers
     */
    public int getEventProviderCount() {
        return this.eventProviders.size();
    }

    /**
     * Gets the identifiers of all event providers of the manager.
     *
     * @return list of provider identifiers
     */
    public List<String> getEventProviderIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        for (EventProvider ep : this.eventProviders) {
            if (ep.getIdentifier() != null) {
                identifiers.add(ep.getIdentifier());
            }
        }
        return identifiers;
    }

    public List<Event> getFilteredEvents(EventFilter filter) { // Täytetään filteredEvents annetun filtterin läpäisseillä
        List<Event> filteredEvents = new ArrayList<>();
        List<Event> allEvents = getAllEvents();

        for (Event event : allEvents) {
            if (filter.accepts(event)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
}
