/* File: BasicProcessScheduler.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A basic round-robin pre-emptive scheduler.
 */


package com.sos.os;

import java.util.LinkedList;

public class BasicProcessScheduler implements ProcessScheduler{
    private final LinkedList<SimProcessInfo> process_queue;
    private int currentIdx;

    public BasicProcessScheduler(){
        process_queue = new LinkedList<>();
        currentIdx = 0;
    }


    @Override
    public void addProcess(SimProcessInfo process) {
        process_queue.add(process);
    }

    @Override
    public int getNextProcess() {
        while(process_queue.size() > 0 && process_queue.peek().getState() == SimProcessState.TERMINATED)
            process_queue.removeFirst();
        if(process_queue.size() == 0)return -1;
        SimProcessInfo info = process_queue.removeFirst();
        process_queue.push(info);
        return info.getPid();
    }
}
