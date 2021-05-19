package dataInfoLogic.DTO.CategorizationDTO;

import dataInfoLogic.DTO.CategorizationDTO.CategoryItem;

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
