/* File: Statistics.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A singleton class for recording SimulatedOS run statistics.
 */


package com.sos.bookkeeping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Statistics {
    //Singleton Methods/Variables
    private static Statistics instance = null;

    public static Statistics getStatLog(){
        if(instance == null)
            instance = new Statistics();
        return instance;
    }

    public static void destroy(){
        instance = null;
    }

    //Instance Methods and Variables
    private final String filename;
    private final HashMap<String, Object> stats;

    private Statistics(){
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        filename = "os_log_" + current.format(format) + ".log";
        stats = new HashMap<>();
    }
}
