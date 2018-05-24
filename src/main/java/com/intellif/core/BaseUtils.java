package com.intellif.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseUtils {

    public static String formateDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
