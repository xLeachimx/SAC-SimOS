/* File: SimProcessControl.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 19 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */

//TODO: Add in error checking (memory, resource, etc)

package com.sos.os;

import com.sos.bookkeeping.Logger;
import com.sos.hardware.SimCPU;
import com.sos.hardware.SimRAM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SimOS {
    private static final int GC_FREQ = 20;
    //Constants
    private final Random rng = new Random();
    //Class variables
    private static int nextPid = 0;

    //Instance variables
    private final HashMap<Integer, SimProcess> processMap;
    private final ProcessScheduler scheduler;
    private final MemoryManager memoryManager;
    private final AccessManager accessManager;
    private final SimCPU cpu;
    private final SimRAM ram;
    private int stepCounter;
    private HashMap<Integer, Integer> resourceMap;

    public SimOS(ProcessScheduler scheduler, MemoryManager memoryManager, AccessManager accessManager){
        processMap = new HashMap<>();
        resourceMap = new HashMap<>();
        this.scheduler = scheduler;
        this.memoryManager = memoryManager;
        this.accessManager = accessManager;
        for(int i = 1;i <= 5;i++)this.accessManager.addResource(i);
        cpu = new SimCPU();
        ram = new SimRAM();
        stepCounter = 0;
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
        SimInstruction instruction = current.getCurrentInstruction();
        memoryManager.requestMemory(process, instruction.getInstructionAddress(), ram);
        if(instruction.isMemoryInstruction())
            memoryManager.requestMemory(process, instruction.getMemoryAccess(), ram);
        else if(instruction.isResourceInstruction()){
            int resource = Math.abs(instruction.getResourceAccess());
            if(instruction.getResourceAccess() < 0){
                accessManager.releaseResource(process, resource);
                if(resourceMap.containsKey(resource)){
                    if(resourceMap.get(resource) != process)
                        Logger.getInstance().log(
                                String.format("Process %d attempted to release resource %d claimed by process %d.",
                                        process, resource, resourceMap.get(resource)));
                    resourceMap.remove(resource);
                }
                else{
                    Logger.getInstance().log(String.format("Process %d attempted release unclaimed resource %d",
                            process, resource));
                }
            }
            else{
                if(!accessManager.requestResource(process, resource)){
                    current.setState(SimProcessState.RESOURCE_HOLD);
                    Logger.getInstance().log(String.format("Process %d held after requesting resource %d.",
                            process, resource));
                }
                else{
                    current.setState(SimProcessState.ACTIVE);
                    Logger.getInstance().log(String.format("Process %d claimed resource %d.",
                            process, resource));
                    if(!resourceMap.containsKey(resource)){
                        resourceMap.put(resource, process);
                    }
                    else if(resourceMap.get(resource) != process){
                        Logger.getInstance().log(String.format("Process %d claimed resource %d held by process %d.",
                            process, resource, resourceMap.get(resource)));
                    }
                }
            }
        }
        if(current.getState() == SimProcessState.WAITING){
            Logger.getInstance().log(String.format("Waiting process %d given CPU burst.", process));
        }
        if(current.getState() != SimProcessState.RESOURCE_HOLD)
            cpu.run_burst(processMap.get(process), process);
        stepCounter += 1;
        if(stepCounter%GC_FREQ == 0) {
            stepCounter = 0;
            collect_garbage();
        }
    }

    public boolean idle(){
        return (processMap.size() == 0);
    }

    public void collect_garbage(){
        ArrayList<Integer> removals = new ArrayList<>();
        for(Integer key : processMap.keySet()){
            if(processMap.get(key).getState() == SimProcessState.COMPLETE){
                Logger.getInstance().log(String.format("Process %d complete and removed from system.", key));
                removals.add(key);
            }
        }
        for(Integer key : removals) processMap.remove(key);
    }
}
