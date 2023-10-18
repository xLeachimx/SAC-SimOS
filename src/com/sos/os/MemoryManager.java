package com.sos.os;

import com.sos.hardware.SimRAM;

public interface MemoryManager {
    void writeRequest(int pid, int addr);
    void readRequest(int pid, int addr);
}
