package edu.dartmouth.cs.pantryplanner.app.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by yangxk15 on 3/1/17.
 */

public interface Constants {
    boolean localMode = false;
    String LOCAL_SERVER_IP = "172.20.10.11";
    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy", Locale.US);
    SimpleDateFormat MINUTE_FORMAT = new SimpleDateFormat("MMM dd yyyy hh:mm", Locale.US);
}