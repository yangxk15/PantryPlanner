package edu.dartmouth.cs.pantryplanner.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 3/3/17.
 */

@AllArgsConstructor
@Data
public class Item {
    String name;
    ItemType itemType;
}
