package org.xi.quick.utils;

public class DateUtils {

    public static java.util.Date getUtilDate(java.util.Date date) {
        return new java.util.Date(date.getTime());
    }

    public static java.sql.Date getSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static java.sql.Time getSqlTime(java.util.Date date) {
        return new java.sql.Time(date.getTime());
    }

    public static java.sql.Timestamp getSqlTimestamp(java.util.Date date) {
        return new java.sql.Timestamp(date.getTime());
    }


    public static java.util.Date getUtilDate(long timestamp) {
        return new java.util.Date(timestamp);
    }

    public static java.sql.Date getSqlDate(long timestamp) {
        return new java.sql.Date(timestamp);
    }

    public static java.sql.Time getSqlTime(long timestamp) {
        return new java.sql.Time(timestamp);
    }

    public static java.sql.Timestamp getSqlTimestamp(long timestamp) {
        return new java.sql.Timestamp(timestamp);
    }
}
