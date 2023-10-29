/* File: SimResource.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Sep 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos.os;

import com.sos.bookkeeping.Logger;

import java.util.HashSet;
import java.util.Objects;

public class SimResource {
    private static int nextID = 0;
    private final int resourceID;
    private final HashSet<ResourceController> controllingProcesses;

    public SimResource(){
        resourceID = SimResource.nextID;
        SimResource.nextID += 1;
        controllingProcesses = new HashSet<>();
    }

    public void releaseControl(int processID){
        for(ResourceController contr : controllingProcesses){
            if(contr.pid == processID){
                controllingProcesses.remove(contr);
                Logger.log_res(String.format("Process %d released resource %d.", processID, resourceID));
                return;
            }
        }
        Logger.error_res(String.format("Process %d attempted to release resource %d, which it did not control.", processID, resourceID));
    }

    public void addController(int processID, boolean write){
        ResourceController contr = new ResourceController(processID, write);
        if(!controllingProcesses.contains(contr)) {
            Logger.log_res(String.format("Process %d now controlling resource %d.", processID, resourceID));
        }
        controllingProcesses.add(contr);
        if(controllingProcesses.size() > 1){
            int writer_count = 0;
            int reader_count = 0;
            for(ResourceController ctrl : controllingProcesses) {
                if (ctrl.writer)
                    writer_count += 1;
                else
                    reader_count += 1;
            }
            if(writer_count > 0){
                Logger.error_res(String.format("Resource %d has more than one controlling process writing to it.", resourceID));
            }
            if(writer_count != 0 && reader_count != 0){
                Logger.error_res(String.format("Resource %d has both reader and writer controlling processes.", resourceID));
            }
        }
    }

    public boolean hasControl(int processID){
        return controllingProcesses.contains(processID);
    }

    public int getID(){
        return resourceID;
    }

    //Private nested class
    private class ResourceController{
        public int pid;
        public boolean writer;
        public ResourceController(int pid, boolean writer){
            this.pid = pid;
            this.writer = writer;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof ResourceController other) {
                return (pid == other.pid) && (writer == other.writer);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(pid, writer);
        }
    }
}
