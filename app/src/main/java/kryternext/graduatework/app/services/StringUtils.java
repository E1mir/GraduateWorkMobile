package kryternext.graduatework.app.services;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class StringUtils {
    public static String getDateFromTimestamp(String timestamp) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy h:m:s", Locale.US).format(new SimpleDateFormat("s", Locale.US).parse(timestamp));
        } catch (ParseException e) {
            Log.e("DATE PARSING", e.getMessage());
            return null;
        }
    }

    public static String getDateFromTimestamp(long timestamp) {
        try {
            String ts = String.valueOf(timestamp);
            return new SimpleDateFormat("dd-MM-yyyy h:m:s", Locale.US).format(new SimpleDateFormat("s", Locale.US).parse(ts));
        } catch (ParseException e) {
            Log.e("DATE PARSING", e.getMessage());
            return null;
        }
    }

    public static String getCapitalizedText(String text) {
        StringBuilder builder = new StringBuilder(text.trim());
        builder.setCharAt(0, Character.toUpperCase(builder.charAt(0)));
        return builder.toString();
    }

}