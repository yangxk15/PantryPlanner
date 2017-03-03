package edu.dartmouth.cs.pantryplanner.backend.entity;

import com.google.appengine.repackaged.com.google.common.base.StringUtil;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Calendar;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by yangxk15 on 2/28/17.
 */

@Entity
@Data
@NoArgsConstructor
public class UserRecord {
    @Id
    Long id;

    @Index
    String email;

    String firstName;
    String lastName;
    String password;

    public UserRecord(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
}
