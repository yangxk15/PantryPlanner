package edu.dartmouth.cs.pantryplanner.app.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static edu.dartmouth.cs.pantryplanner.app.util.Constants.DATE_FORMAT;
import static edu.dartmouth.cs.pantryplanner.app.util.Constants.MINUTE_FORMAT;

/**
 * Created by yangxk15 on 3/5/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Getter
public class PantryItem implements Comparable<PantryItem>{
    Date productionDate;
    Item item;

    @Override
    public int hashCode() {
        return MINUTE_FORMAT.format(productionDate).hashCode() + item.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        PantryItem pantryItem = (PantryItem) o;
        return MINUTE_FORMAT.format(pantryItem.productionDate).equals(MINUTE_FORMAT.format(productionDate))
                && pantryItem.item.equals(item);
    }

    @Override
    public int compareTo(PantryItem other){
        try {
            Date d1 = MINUTE_FORMAT.parse(MINUTE_FORMAT.format(productionDate));
            Date d2 = MINUTE_FORMAT.parse(MINUTE_FORMAT.format(other.productionDate));
            return d1.compareTo(d2);
        } catch (ParseException e) {
            return 0;
        }
    }



    public int getLeftDays() {
        Calendar calendar = Calendar.getInstance();
//        long daysPassed = (calendar.getTime().getTime() - productionDate.getTime()) / (1000 * 60 * 60 * 24);
//        return item.itemType.getFreshTime() - (int) daysPassed;
        long minutesPassed = (calendar.getTime().getTime() - productionDate.getTime()) / (1000 * 60);
        return item.itemType.getFreshTime() - (int) minutesPassed;
    }
}
