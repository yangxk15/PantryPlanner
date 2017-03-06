package edu.dartmouth.cs.pantryplanner.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by yangxk15 on 3/1/17.
 */

public interface Constants {
    boolean localMode = true;
    String LOCAL_SERVER_IP = "192.168.2.13";
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy", Locale.US);
}
