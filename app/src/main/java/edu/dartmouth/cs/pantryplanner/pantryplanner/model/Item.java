package edu.dartmouth.cs.pantryplanner.pantryplanner.model;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 2/27/17.
 */

@Data
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

    public int getCountdownDays() {
        long diff = Calendar.getInstance().getTimeInMillis() - timestamp.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }
}
