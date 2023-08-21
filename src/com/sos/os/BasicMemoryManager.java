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
    private final Random rng;
    public BasicMemoryManager(){
        rng = new Random();
    }
    @Override
    public void requestMemory(int pid, int addr, SimRAM ram) {
        int page = addr / ram.get_page_size();
        for(int i = 0;i < ram.num_pages();i++) {
            //Page already in memory
            if(page == ram.get_process_page(page))return;
        }
        Logger.getInstance().log(String.format("Page fault for process %d page %d.", pid, page));
        int free = ram.nextFree();
        if(free == -1){
            free = rng.nextInt(ram.num_pages());
            Logger.getInstance().log(String.format("No free page. Deallocating page %d in RAM.", free));
            ram.free(free);
        }
        ram.allocate(free, pid, page);
    }
}
