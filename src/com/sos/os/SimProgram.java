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
    private static final int instrSize = 4;
    private static final int minSize = 100;
    private static final int maxSize = 1024;
    private static final int jumpChance = 5;
    private int codeSpace;
    private int variableSpace;
    private ArrayList<Instruction> code;

    public SimProgram(){
        generate();
    }

    public int total_size(){
        return (instrSize * codeSpace) + variableSpace;
    }

    public Instruction getInstr(int idx){
        return code.get(idx);
    }
    public boolean validInstr(int idx){
        return idx >= 0 && idx < code.size();
    }

    public int completionTime(){
        int total = 0;
        for(Instruction instr : code){
            total += instr.cycleCount;
        }
        return total;
    }

    //=========================
    //  Private Functions
    //=========================
    private void generate(){
        //Generate Code
        codeSpace = rng.nextInt(minSize, maxSize);
        //Determine jumps
        int[] nextInstrs = shuffleInstrJump(codeSpace);
        variableSpace = rng.nextInt(minSize, 2*maxSize);
        for(int i = 0;i < codeSpace;i++){
            int instrAddr = instrSize * i;
            Instruction temp = new Instruction(instrAddr, nextInstrs[i]);
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

    private int[] shuffleInstrJump(int n){
        int[] idxs = new int[n-1];
        for(int i = 0;i < idxs.length;i++){
            idxs[i] = i+1;
        }
        for(int i = 0;i < idxs.length-1;i++){
            if(rng.nextInt(100) < jumpChance){
                int jump = rng.nextInt(i+1, idxs.length);
                int temp = idxs[i];
                idxs[i] = idxs[jump];
                idxs[jump] = temp;
            }
        }
        int[] result = new int[n];
        Arrays.fill(result, -1);
        int lastIdx = 0;
        for (int idx : idxs) {
            result[lastIdx] = idx;
            lastIdx = idx;
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
        private int instrAddr;
        private int nextInstr;
        private int cycleCount;

        public Instruction(int instrAddr, int nextInstr){
            this.instrAddr = instrAddr;
            this.nextInstr = nextInstr;
            cycleCount = rng.nextInt(32) + 1;
        }

        public int getInstrAddr(){
            return instrAddr;
        }

        public int getNextInstr(){
            return nextInstr;
        }

        public int getCycleCount(){
            return cycleCount;
        }
    }

}
