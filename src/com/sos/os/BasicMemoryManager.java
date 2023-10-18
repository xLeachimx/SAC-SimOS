/* File: BasicMemoryManager.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 20 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;
import com.sos.hardware.SimRAM;
import java.util.Random;

public class BasicMemoryManager implements MemoryManager{
    public BasicMemoryManager(){}

    @Override
    public void writeRequest(int pid, int addr){
        int page = addr / SimRAM.getInstance().getPageSize();
        SimRAM.getInstance().free(0);
        SimRAM.getInstance().store(new SimPage(pid, page), 0);
    }

    @Override
    public void readRequest(int pid, int addr){
        int page = addr / SimRAM.getInstance().getPageSize();
        SimRAM.getInstance().free(0);
        SimRAM.getInstance().store(new SimPage(pid, page), 0);
    }
}
