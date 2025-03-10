package tamk.ohsyte;

import java.time.MonthDay;

public class DateCategoryFilter extends EventFilter {
    private MonthDay monthDay;
    private Category category;

    public DateCategoryFilter(MonthDay monthDay, Category category) {
        this.monthDay = monthDay;
        this.category = category;
    }

    @Override
    public boolean accepts(Event event) {
        Category eventCategory = event.getCategory();
        if (category.getSecondary() == null) {
            return eventCategory.getPrimary().equals(category.getPrimary())
                    && eventCategory.getSecondary() == null
                    && event.getMonthDay().equals(monthDay);
        }
        return eventCategory.equals(category) && event.getMonthDay().equals(monthDay);
    }

}
