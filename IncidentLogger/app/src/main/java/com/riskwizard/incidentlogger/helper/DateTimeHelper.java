package com.riskwizard.incidentlogger.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;


public final class DateTimeHelper {

    public final static String TAG = "DATE_TIME_HELPER";

    private static final SimpleDateFormat DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    //new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private static final SimpleDateFormat TIME_FORMATTER =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    private static final SimpleDateFormat VERBOSE_LOCALE_DATE_FORMATTER =
            new SimpleDateFormat("MMM dd',' yyyy", Locale.getDefault());

    private static final SimpleDateFormat VERBOSE_LOCALE_TIME_FORMATTER =
            new SimpleDateFormat("hh:mm a", Locale.getDefault());

    private static final SimpleDateFormat VERBOSE_LOCALE_DATE_TIME_FORMATTER =
            new SimpleDateFormat("MMM  dd',' yyyy hh:mm a", Locale.getDefault());

    private static final SimpleDateFormat ISO_8601_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS", Locale.getDefault());
    private static final SimpleDateFormat ALMOST_ISO_8601_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());

    public static Date parseAlmostISO8601DateTimeWithTSeparator(String datetime) {
        Log.d(TAG, "datetime="+ datetime);
        try {
            return ALMOST_ISO_8601_FORMATTER.parse(datetime);
        } catch (ParseException e) {
            Log.e(TAG, "caught ParseException", e);
            return null;
        }
    }

    /**
     * Parses an ISO8601 formatted datetime and returns a
     * java.util.Date object for it, or NULL if parsing
     * the date fails.
     *
     * @param String datetime
     * @return Date|null
     */
    public static Date parseFromISO8601(String datetime) {
        Log.d(TAG, "datetime="+ datetime);
        try {
            return ISO_8601_FORMATTER.parse(datetime);
        } catch (ParseException e) {
            Log.e(TAG, "caught ParseException", e);
            return null;
        }
    }

    public static String formatToIS08601(Date date) {
        return ISO_8601_FORMATTER.format(date);
    }

    public static Date parseOnlyDate(String date) {
        Log.d(TAG, "date="+ date);
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            Log.e(TAG, "caught ParseException", e);
            return null;
        }
    }

    /**
     * Tries to parse most date times that it is passed, using
     * some heuristics.
     *
     * @param string
     * @return
     */
    public static Date parseDateTime(String datetime) {
        if (datetime.contains("T")) {
            Log.d(TAG, "parseDateTime(): Trying ISO8601 with a T separator");
            return parseAlmostISO8601DateTimeWithTSeparator(datetime);
        } else if (datetime.length() == 10 && datetime.contains("-")) {
            Log.d(TAG, "parseDateTime(): Trying just yyyy-MM-dd date");
            return parseOnlyDate(datetime);
        } else {
            Log.d(TAG, "parseDateTime(): Trying regular ISO8601");
            return parseFromISO8601(datetime);
        }
    }

    public static Date parseTime(String time) {
        try {
            return TIME_FORMATTER.parse(time);
        } catch (ParseException e) {
            Log.d(TAG, "parseTime() caught ParseException", e);
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String date) {
        try {
            return DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            Log.d(TAG, "parseDate() caught ParseException", e);
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseDateAndTime(String strDate, String strTime) {
        Date date = parseDate(strDate);
        Date time = parseTime(strTime);

        if (date == null) {
            try {
                date = VERBOSE_LOCALE_DATE_FORMATTER.parse(strDate);
            } catch (ParseException e) {
                Log.d(TAG, "parseDate() caught ParseException", e);
                e.printStackTrace();
                return null;
            }
        }

        if (time == null) {
            try {
                time = VERBOSE_LOCALE_TIME_FORMATTER.parse(strTime);
            } catch (ParseException e) {
                Log.d(TAG, "parseTime() caught ParseException", e);
                e.printStackTrace();
                return null;
            }
        }

        //long dateMillis = date.getTime();
        //long timeMillis = time.getTime();
        //return new Date(dateMillis + timeMillis);

        return new Date(
                date.getYear(), date.getMonth(), date.getDay(),
                time.getHours(), time.getMinutes(), time.getSeconds()
        );
    }

    public static String pad(int value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

    public static String toLocaleDateTime(Date date) {
        return VERBOSE_LOCALE_DATE_TIME_FORMATTER.format(date);
    }

    public static String toLocaleDate(Date date) {
        return VERBOSE_LOCALE_DATE_FORMATTER.format(date);
    }

    public static String toLocaleTime(Date date) {
        return VERBOSE_LOCALE_TIME_FORMATTER.format(date);
    }

    public static Date toLocalTime(Date date) {
        long millisUTC = date.getTime();
        TimeZone tz = TimeZone.getDefault();
        int tzOffset = tz.getOffset(millisUTC);
        if (tz.inDaylightTime(new Date(millisUTC))) {
            millisUTC -= tz.getDSTSavings();
        }
        return new Date(millisUTC + tzOffset);
    }

    public static long toLocalTime(long millisUTC) {
        // Refactor this and save it. Perfect DST code.
        TimeZone tz = TimeZone.getDefault();
        int tzOffset = tz.getOffset(millisUTC);
        if (tz.inDaylightTime(new Date(millisUTC))) {
            millisUTC -= tz.getDSTSavings();
        }
        return millisUTC + tzOffset;
    }

}
