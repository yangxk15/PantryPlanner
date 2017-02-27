package edu.dartmouth.cs.pantryplanner.pantryplanner.model;

import java.util.Calendar;

import lombok.AllArgsConstructor;

/**
 * Created by yangxk15 on 2/27/17.
 */

public class Item {
    String id;
    String name;
    ItemType itemType;
    Calendar timestamp;

    public Item(String name, ItemType itemType) {
        this.name = name;
        this.itemType = itemType;
        this.timestamp = Calendar.getInstance();
        this.id = name + timestamp.getTimeInMillis();
    }
}
