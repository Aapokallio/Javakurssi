package tamk.ohsyte;

public class CategoryFilter extends EventFilter {
    private Category category;

    public CategoryFilter(Category category) {
        this.category = category;
    }

    @Override
    public boolean accepts(Event event) {
        Category eventCategory = event.getCategory();

        if (category.getSecondary() == null) {
            return eventCategory.getPrimary().equals(category.getPrimary()) && eventCategory.getSecondary() == null;
        }
        return eventCategory.equals(category);
    }
}
