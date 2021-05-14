package dataInfoLogic.DataTypes;

import java.util.LinkedList;

public class CategoryList {

    LinkedList<CategoryItem> categories;

    public LinkedList<CategoryItem> getCategories() {
        return categories;
    }

    public void setCategories(LinkedList<CategoryItem> categories) {
        this.categories = categories;
    }
}
