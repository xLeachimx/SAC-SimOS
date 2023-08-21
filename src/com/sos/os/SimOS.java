/* File: SimProcessControl.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;
import com.sos.hardware.SimCPU;
import com.sos.hardware.SimRAM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimOS {
    //Constants
    private final Random rng = new Random();
    //Class variables
    private static int nextPid = 0;

    //Instance variables
    private final HashMap<Integer, SimProcess> processMap;
    private final ProcessScheduler scheduler;
    private final MemoryManager memoryManager;
    private final SimCPU cpu;
    private final SimRAM ram;

    public SimOS(ProcessScheduler scheduler, MemoryManager memoryManager){
        processMap = new HashMap<>();
        this.scheduler = scheduler;
        this.memoryManager = memoryManager;
        cpu = new SimCPU();
        ram = new SimRAM();
    }

    public void add_process(SimProcess process){
        int pid = nextPid++;
        int priority = rng.nextInt(7);
        processMap.put(pid, process);
        Logger.getInstance().log(String.format("New process added. Pid: %d and Priority: %d.", pid, priority));
        scheduler.addProcess(new SimProcessInfo(process,pid, priority));
    }

    public void run_step(){
        int process = scheduler.getNextProcess();
        if(process == -1){
            Logger.getInstance().log("No active process. Idling.");
            return;
        }
        SimProcess current = processMap.get(process);
        Logger.getInstance().log(String.format("Running burst on process %d.", process));
        int[] mem_req = current.getNeededMemoryAddr();
        for(int addr : mem_req) {
            memoryManager.requestMemory(process, addr, ram);
        }
        cpu.run_burst(processMap.get(process), process);
        collect_garbage();
    }

    public void collect_garbage(){
        ArrayList<Integer> removals = new ArrayList<>();
        for(Integer key : processMap.keySet()){
            if(processMap.get(key).get_state() == SimProcessState.COMPLETE) removals.add(key);
        }
        for(Integer key : removals) processMap.remove(key);
    }
}
