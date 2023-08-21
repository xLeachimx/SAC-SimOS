/* File: SimProgram.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 17 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.os.instructions.MemoryInstruction;
import com.sos.os.instructions.OperationInstruction;
import com.sos.os.instructions.ResourceInstruction;

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
    private ArrayList<SimInstruction> code;

    public SimProgram(){
        generate();
    }

    public int total_size(){
        return (instrSize * codeSpace) + variableSpace;
    }

    public SimInstruction getInstr(int idx){
        return code.get(idx);
    }
    public boolean validInstr(int idx){
        return idx >= 0 && idx < code.size();
    }

    public int completionTime(){
        int total = 0;
        for(SimInstruction instr : code){
            total += instr.getCycleCount();
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
        int last_variable = 0;
        for(int i = 0;i < codeSpace;i++){
            int instrAddr = instrSize * i;
            //Randomly determine instruction type.
            int roll = rng.nextInt(100);
            SimInstruction temp;
            if(roll < 70){
                //Simple operation instruction (~70%)
                temp = new OperationInstruction(instrAddr, nextInstrs[i]);
            }
            else if(roll < 90){
                //Memory operation instruction (~20%)
                int memoryRoll = rng.nextInt(100);
                int address;
                if(memoryRoll < 40){
                    //Memory access is near last access (~40%, spatial locality)
                    address = last_variable + (rng.nextInt(10) - 5);
                    address = Math.max(0, Math.min(address, variableSpace-1));
                }
                else if(memoryRoll < 60){
                    //Memory access is the same as last access (~20%, temporal locality)
                    address = last_variable;
                }
                else{
                    //Memory access is somewhere random (~40%, no locality)
                    address = rng.nextInt(variableSpace);
                }
                last_variable = address;
                temp = new MemoryInstruction(instrAddr, nextInstrs[i], address);
            }
            else{
                //Resource allocation instruction (~10%)
                int resource = rng.nextInt(5);
                temp = new ResourceInstruction(instrAddr, nextInstrs[i], resource);
            }
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
}
