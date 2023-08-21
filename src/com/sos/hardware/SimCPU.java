/* File: SimCPU.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.hardware;

import com.sos.bookkeeping.Logger;
import com.sos.os.SimProcess;

public class SimCPU {
    //Instance variables
    private int cycleCount;
    private final int burstAmount;

    public SimCPU(){
        cycleCount = 0;
        burstAmount = 10;
    }

    public SimCPU(int burstAmount){
        cycleCount = 0;
        this.burstAmount = burstAmount;
    }

    public void run_burst(SimProcess proc, int pid){
        int cycles = proc.run_cycles(burstAmount);
        Logger.getInstance().log(String.format("Ran %d cycles on process %d.", cycles, pid));
        cycleCount += proc.run_cycles(burstAmount);
    }
}
