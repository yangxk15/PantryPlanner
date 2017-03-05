package edu.dartmouth.cs.pantryplanner.app.model;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 3/3/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class Item {
    String name;
    ItemType itemType;
}
