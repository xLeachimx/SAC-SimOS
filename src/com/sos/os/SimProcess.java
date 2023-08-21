/* File: SimProcess.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 * Notes:
 *  Add in memory access system.
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;

public class SimProcess {
    private final SimProgram baseProgram;
    private int cycleCount;
    private int programInstruction;
    private int remainingCyclesOnInstr;
    private SimProcessState state;

    public SimProcess(SimProgram base_program){
        this.baseProgram = base_program;
        cycleCount = 0;
        programInstruction = 0;
        remainingCyclesOnInstr = this.baseProgram.get_instr(programInstruction).get_cycle_count();
        state = SimProcessState.WAITING;
    }

    public int run_cycles(int cycles){
        if(state == SimProcessState.WAITING || state == SimProcessState.COMPLETE){
            Logger.getInstance().log("Attempted to run a WAITING or COMPLETE process.");
            return 0;
        }
        if(state == SimProcessState.RESOURCE_HOLD){
            Logger.getInstance().log("Attempted to run a RESOURCE_HOLD process.");
            return 0;
        }
        for(int i = 0;i < cycles;i++) {
            remainingCyclesOnInstr -= 1;
            cycleCount += 1;
            //Move to next instructions
            if (remainingCyclesOnInstr <= 0) {
                programInstruction = baseProgram.get_instr(programInstruction).get_next_instr();
                if(!baseProgram.valid_instr(programInstruction)){
                    state = SimProcessState.COMPLETE;
                    return i;
                }
                remainingCyclesOnInstr = baseProgram.get_instr(programInstruction).get_cycle_count();
            }
        }
        return cycles;
    }

    public int[] getNeededMemoryAddr(){
        if(state == SimProcessState.WAITING || state == SimProcessState.COMPLETE)return null;
        int[] result = new int[1];
        result[0] = baseProgram.get_instr(programInstruction).get_instr_addr();
        return result;
    }

    public void setState(SimProcessState new_state){
        state = new_state;
    }

    public int completionTime(){
        return (baseProgram.completion_time() - cycleCount);
    }

    public SimProcessState getState(){
        return state;
    }
}
