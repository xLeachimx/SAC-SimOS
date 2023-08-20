/* File: SimProcessControl.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.hardware.SimCPU;
import com.sos.hardware.SimRAM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimOS {
    //Constants
    private final Random rng = new Random();
    //Class variables
    private static int next_pid = 0;

    //Instance variables
    private final HashMap<Integer, SimProcess> process_map;
    private final ProcessScheduler scheduler;
    private final MemoryManager memory_manager;
    private final SimCPU cpu;
    private final SimRAM ram;

    public SimOS(ProcessScheduler scheduler, MemoryManager memory_manager){
        process_map = new HashMap<>();
        this.scheduler = scheduler;
        this.memory_manager = memory_manager;
        cpu = new SimCPU();
        ram = new SimRAM();
    }

    public void add_process(SimProcess process){
        int pid = next_pid++;
        int priority = rng.nextInt(7);
        process_map.put(pid, process);
        scheduler.add_process(new SimProcessInfo(process,pid, priority));
    }

    public void run_step(){
        int process = scheduler.get_next_process();
        if(process == -1)return;
        SimProcess current = process_map.get(process);
        int[] mem_req = current.get_needed_memory_addr();
        for(int addr : mem_req) {
            memory_manager.request_memory(process, addr, ram);
        }
        cpu.run_burst(process_map.get(process));
    }

    public void collect_garbage(){
        ArrayList<Integer> removals = new ArrayList<>();
        for(Integer key : process_map.keySet()){
            if(process_map.get(key).get_state() == SimProcessState.COMPLETE) removals.add(key);
        }
        for(Integer key : removals) process_map.remove(key);
    }
}
