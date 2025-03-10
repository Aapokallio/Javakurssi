package tamk.ohsyte;

import java.time.MonthDay;

public class DateFilter extends EventFilter {
    private Integer year;
    private MonthDay monthDay;

    public DateFilter(MonthDay monthDay) {
        this.monthDay = monthDay;
        this.year = null;
    }

    public DateFilter(MonthDay monthDay, int year) {
        this.monthDay = monthDay;
        this.year = year;
    }

    @Override
    public boolean accepts(Event event) {
        if (!event.getMonthDay().equals(monthDay)) {
            return false;
        }

        if (year == null) {
            return true;
        }

        if (event instanceof SingularEvent) {
            SingularEvent singularEvent = (SingularEvent) event; //tyyppimuunnos, kun varmistus mennyt läpi
            return singularEvent.getYear() == year;
        }

        if (event instanceof AnnualEvent) {
            return true;
        }
        return false;
    }
}
