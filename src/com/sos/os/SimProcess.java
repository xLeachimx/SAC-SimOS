/* File: SimProcess.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 * Notes:
 *  Add in memory access system.
 */


package com.sos.os;

public class SimProcess {
    private final SimProgram base_program;
    private int cycle_count;
    private int program_instruction;
    private int remaining_cycles_on_instr;
    private SimProcessState state;

    public SimProcess(SimProgram base_program){
        this.base_program = base_program;
        cycle_count = 0;
        program_instruction = 0;
        remaining_cycles_on_instr = this.base_program.get_instr(program_instruction).get_cycle_count();
        state = SimProcessState.WAITING;
    }

    public int run_cycles(int cycles){
        if(state == SimProcessState.WAITING || state == SimProcessState.COMPLETE)return 0;
        for(int i = 0;i < cycles;i++) {
            remaining_cycles_on_instr -= 1;
            cycle_count += 1;
            //Move to next instructions
            if (remaining_cycles_on_instr <= 0) {
                program_instruction = base_program.get_instr(program_instruction).get_next_instr();
                if(!base_program.valid_instr(program_instruction)){
                    state = SimProcessState.COMPLETE;
                    return i;
                }
                remaining_cycles_on_instr = base_program.get_instr(program_instruction).get_cycle_count();
            }
        }
        return cycles;
    }

    public int[] get_needed_memory_addr(){
        if(state == SimProcessState.WAITING || state == SimProcessState.COMPLETE)return null;
        int[] result = new int[1];
        result[0] = base_program.get_instr(program_instruction).get_instr_addr();
        return result;
    }

    public void set_state(SimProcessState new_state){
        state = new_state;
    }

    public int completion_time(){
        return (base_program.completion_time() - cycle_count);
    }

    public SimProcessState get_state(){
        return state;
    }
}
