/* File: ResourceInstruction.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os.instructions;

import com.sos.os.SimInstruction;
import com.sos.os.SimInstructionType;

public class ResourceInstruction implements SimInstruction {

    public static final int RESOURCE_CYCLES = 10;
    private final int address;
    private final int next;
    private final int resource;
    private final boolean request;

    public ResourceInstruction(int address, int next, int resource, boolean request){
        this.address = address;
        this.next = next;
        this.resource = resource;
        this.request = request;
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
        return RESOURCE_CYCLES;
    }

    @Override
    public SimInstructionType getType(){
        return SimInstructionType.RESOURCE;
    }

    public boolean isRequest() {
        return request;
    }

    public int getResource(){
        return resource;
    }
}
