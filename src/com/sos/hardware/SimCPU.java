/* File: SimCPU.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.hardware;

import com.sos.os.SimProcess;

public class SimCPU {
    //Instance variables
    private int cycle_count;
    private final int burst_amount;

    public SimCPU(){
        cycle_count = 0;
        burst_amount = 10;
    }

    public SimCPU(int burst_amount){
        cycle_count = 0;
        this.burst_amount = burst_amount;
    }

    public void run_burst(SimProcess proc){
        cycle_count += proc.run_cycles(burst_amount);
    }
}
