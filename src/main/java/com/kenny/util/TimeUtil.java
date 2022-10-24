package com.kenny.util;

import java.sql.Timestamp;

public class TimeUtil {
    public static Long calTimeDifference(Timestamp end, Timestamp start){
        return end.getTime() - start.getTime();
    }
}
