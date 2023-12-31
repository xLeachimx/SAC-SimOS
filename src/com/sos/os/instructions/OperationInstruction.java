/* File: OperationInstruction.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os.instructions;

import com.sos.os.SimInstruction;
import com.sos.os.SimInstructionType;

public class OperationInstruction implements SimInstruction {
    public static final int OPERATION_CYCLES = 1;
    private final int address;
    private final int next;

    public OperationInstruction(int address, int next) {
        this.address = address;
        this.next = next;
    }


    @Override
    public int getInstructionAddress() {
        return address;
    }

    @Override
    public int getNextInstructionIndex() {
        return next;
    }

    @Override
    public int getCycleCount() {
        return OPERATION_CYCLES;
    }

    @Override
    public SimInstructionType getType(){
        return SimInstructionType.OPERATION;
    }
}
