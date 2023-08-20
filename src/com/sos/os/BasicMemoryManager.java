/* File: BasicMemoryManager.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 20 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.hardware.SimRAM;
import java.util.Random;

public class BasicMemoryManager implements MemoryManager{
    private final Random rng;
    public BasicMemoryManager(){
        rng = new Random();
    }
    @Override
    public void request_memory(int pid, int addr, SimRAM ram) {
        int page = addr / ram.get_page_size();
        for(int i = 0;i < ram.num_pages();i++) {
            ram.get_process_page(page);
        }
        int free = ram.nextFree();
        if(free == -1){
            free = rng.nextInt(ram.num_pages());
            ram.free(free);
        }
        ram.allocate(free, pid, page);
    }
}
