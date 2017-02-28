package edu.dartmouth.cs.pantryplanner.pantryplanner.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by yangxk15 on 2/27/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
@Data
public class User {
    String firstName;
    String lastName;
    String email;
    String password;
}
