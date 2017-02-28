package edu.dartmouth.cs.pantryplanner.pantryplanner.util;

/**
 * Created by yangxk15 on 2/28/17.
 */

public class EmailValidator {
    public static boolean validate(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
