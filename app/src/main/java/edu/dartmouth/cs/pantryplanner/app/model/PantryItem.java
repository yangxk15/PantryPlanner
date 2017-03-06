package edu.dartmouth.cs.pantryplanner.app.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by yangxk15 on 3/5/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class PantryItem {
    Date productionDate;
    Item item;

    @Override
    public int hashCode() {
        return productionDate.hashCode() + item.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return ((PantryItem) o).productionDate.equals(productionDate) && ((PantryItem) o).item.equals(item);
    }
}
