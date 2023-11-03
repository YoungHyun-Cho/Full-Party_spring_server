package com.full_party;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        Date expiration = calendar.getTime();

//        System.out.println("calendar.getTime() = " + calendar.getTime());
//        System.out.println("calendar.getTimeZone() = " + calendar.getTimeZone());
//        System.out.println("expiration = " + expiration);

        System.out.println(Duration.ofMinutes(30).getSeconds());
        /*
        * 2023-11-03 T07:43:15.377Z
        * */
    }
}
