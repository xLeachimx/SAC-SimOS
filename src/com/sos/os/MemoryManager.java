package com.sos.os;

import com.sos.hardware.SimRAM;

public interface MemoryManager {
    void request_memory(int pid, int addr, SimRAM ram);
}
