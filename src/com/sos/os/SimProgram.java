/* File: SimProgram.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SimProgram {
    private static final Random rng = new Random();
    private static final int instr_size = 4;
    private static final int min_size = 100;
    private static final int max_size = 1024;
    private static final int jump_chance = 5;
    private int code_space;
    private int variable_space;
    private ArrayList<Instruction> code;

    public SimProgram(){
        generate();
    }

    public int total_size(){
        return (instr_size * code_space) + variable_space;
    }

    public Instruction get_instr(int idx){
        return code.get(idx);
    }
    public boolean valid_instr(int idx){
        return idx >= 0 && idx < code.size();
    }

    public int completion_time(){
        int total = 0;
        for(Instruction instr : code){
            total += instr.cycle_count;
        }
        return total;
    }

    //=========================
    //  Private Functions
    //=========================
    private void generate(){
        //Generate Code
        code_space = rng.nextInt(min_size, max_size);
        //Determine jumps
        int[] next_instrs = shuffle_instr_jump(code_space);
        variable_space = rng.nextInt(min_size, 2*max_size);
        for(int i = 0;i < code_space;i++){
            int instr_addr = instr_size * i;
            Instruction temp = new Instruction(instr_addr, next_instrs[i]);
            code.add(temp);
        }
    }

    /* Python version of index shuffling (no locality)
    def shuffle_instr_easy(n):
        idxs = [i+1 for i in range(n-1)]
        shuffle(idxs)
        result = [-1 for i in range(n)]
        last_idx = 0
        for idx in idxs:
            result[last_idx] = idx
            last_idx = idx
        result[result.index(-1)] = n
        return result
     */

    private int[] shuffle_instr_jump(int n){
        int[] idxs = new int[n-1];
        for(int i = 0;i < idxs.length;i++){
            idxs[i] = i+1;
        }
        for(int i = 0;i < idxs.length-1;i++){
            if(rng.nextInt(100) < jump_chance){
                int jump = rng.nextInt(i+1, idxs.length);
                int temp = idxs[i];
                idxs[i] = idxs[jump];
                idxs[jump] = temp;
            }
        }
        int[] result = new int[n];
        Arrays.fill(result, -1);
        int last_idx = 0;
        for (int idx : idxs) {
            result[last_idx] = idx;
            last_idx = idx;
        }
        for(int i = 0;i < result.length;i++){
            if(result[i] == -1){
                result[i] = n;
                break;
            }
        }
        return result;
    }

    public class Instruction{
        private int instr_addr;
        private int next_instr;
        private int cycle_count;

        public Instruction(int instr_addr, int next_instr){
            this.instr_addr = instr_addr;
            this.next_instr = next_instr;
            cycle_count = rng.nextInt(32) + 1;
        }

        public int get_instr_addr(){
            return instr_addr;
        }

        public int get_next_instr(){
            return next_instr;
        }

        public int get_cycle_count(){
            return cycle_count;
        }
    }

}
