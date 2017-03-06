package edu.dartmouth.cs.pantryplanner.backend.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangxk15 on 3/5/17.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListRecord {
    @Id
    Long id;

    @Index
    String email;

    String shoppingList;
}
