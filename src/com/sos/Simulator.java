/* File: Simulator.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 21 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *
 */


package com.sos;

import com.sos.bookkeeping.Logger;
import com.sos.generator.CentralRandom;
import com.sos.generator.SimUser;
import com.sos.os.*;

import java.util.ArrayList;

public class Simulator {
    public static void main(String[] args) {
        CentralRandom.getRNG(6942042L);
        Logger.getLog();
        //Create and setup users
        ArrayList<SimUser> users = new ArrayList<>();
        int numUsers = CentralRandom.getRNG().nextInt(20) + 1;
        for(int i = 0;i < numUsers;i++){
            int userProcesses = CentralRandom.getRNG().nextInt(100) + 1;
            int userPrograms = CentralRandom.getRNG().nextInt(10) + 1;
            users.add(new SimUser(userPrograms, userProcesses));
        }
        //Setup simulated operating system
        ProcessScheduler ps = new BasicProcessScheduler();
        MemoryManager mm = new BasicMemoryManager();
        AccessManager am = new BasicResourceManager();
        SimOS operatingSystem = new SimOS(ps, mm, am);
        //Run the operating system step by step
        while(!finished(operatingSystem, users)){
            for(SimUser user : users)
                user.nextStep(operatingSystem);
            operatingSystem.run_step();
        }
        Logger.destroy();
    }

    private static boolean finished(SimOS os, ArrayList<SimUser> users){
        for(SimUser user : users){
            if(!user.finished())return false;
        }
        return os.idle();
    }
}
