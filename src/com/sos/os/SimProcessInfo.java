/* File: SimProcessInfo.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import java.util.Random;

public class SimProcessInfo {
    private static final Random rng = new Random();
    private final SimProcess process;
    private int est_comp_time;
    private final int pid;
    private final int priority;

    public SimProcessInfo(SimProcess process, int pid, int priority){
        this.process = process;
        this.pid = pid;
        this.priority = priority;
        calculate_completion_time();
    }

    public int get_est_comp_time(){
        return est_comp_time;
    }

    public int get_pid(){
        return pid;
    }

    public int get_priority(){
        return priority;
    }

    public SimProcessState get_state(){
        return process.get_state();
    }

    public void set_state(SimProcessState state){
        process.set_state(state);
    }

    public int calculate_completion_time(){
        double error = 1.0 + ((0.2 * rng.nextDouble()) - 0.4);
        est_comp_time = (int)(error * process.completion_time());
        return est_comp_time;
    }
}
