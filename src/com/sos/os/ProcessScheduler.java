package com.sos.os;

public interface ProcessScheduler {
    void add_process(SimProcessInfo process);
    int get_next_process();
}
