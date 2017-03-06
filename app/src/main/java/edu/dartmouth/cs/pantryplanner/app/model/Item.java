package edu.dartmouth.cs.pantryplanner.app.model;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Created by yangxk15 on 3/3/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class Item {
    String name;
    ItemType itemType;

    @Override
    public int hashCode() {
        return name.hashCode() + itemType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return ((Item) o).name.equals(this.name) && ((Item) o).getItemType() == itemType;
    }
}
