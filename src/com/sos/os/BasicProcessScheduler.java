/* File: BasicProcessScheduler.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import java.util.ArrayList;

public class BasicProcessScheduler implements ProcessScheduler{
    private final ArrayList<SimProcessInfo> process_queue;

    public BasicProcessScheduler(){
        process_queue = new ArrayList<>();
    }


    @Override
    public void add_process(SimProcessInfo process) {
        process_queue.add(process);
    }

    @Override
    public int get_next_process() {
        while(process_queue.size() > 0 && process_queue.get(0).get_state() == SimProcessState.COMPLETE){
            process_queue.remove(0);
        }
        if(process_queue.size() == 0)return -1;
        return process_queue.get(0).get_pid();
    }
}
