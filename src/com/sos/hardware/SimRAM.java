/* File: SimRAM.java
 * Author: Dr. Michael Andrew Huelsman
 * Created On: 16 Aug 2023
 * Licence: GNU GPLv3
 * Purpose:
 *  A class for handling simulated RAM.
 */


package com.sos.hardware;

public class SimRAM {
    //Constants
    private static final int default_size = 30*1024; //30KB default ram
    private static final int default_page_size = 1024; //1KB pages

    //Instance Variables
    private int size;
    private int page_size;
    private PageInfo[] pages;

    public SimRAM(){
        size = default_size;
        page_size = default_page_size;
        pages = new PageInfo[size/page_size];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public SimRAM(int page_size){
        size = default_size;
        this.page_size = page_size;
        pages = new PageInfo[size/this.page_size];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public SimRAM(int size, int page_size){
        this.size = size;
        this.page_size = page_size;
        pages = new PageInfo[this.size/this.page_size];
        for(int i = 0;i < pages.length;i++){
            pages[i] = new PageInfo();
        }
    }

    public int num_pages(){
        return pages.length;
    }

    public int nextFree(){
        for(int i = 0;i < pages.length;i++){
            if(!pages[i].allocated)return i;
        }
        return -1;
    }

    public boolean allocate(int page, int pid, int process_page){
        if(page < 0 || page >= pages.length)return false;
        if(pages[page].allocated)return false;
        pages[page].pid = pid;
        pages[page].process_page = process_page;
        pages[page].allocated = true;
        return true;
    }

    public boolean free(int page){
        if(page < 0 || page >= pages.length)return false;
        pages[page].allocated = false;
        return true;
    }

    public void process_clear(int pid){
        for(PageInfo page : pages){
            if(page.pid == pid)page.allocated = false;
        }
    }

    public int get_page_pid(int page){
        if(page < 0 || page >= pages.length)return -1;
        return pages[page].pid;
    }

    public int get_process_page(int page){
        if(page < 0 || page >= pages.length)return -1;
        return pages[page].process_page;
    }

    public int get_page_size(){
        return page_size;
    }

    public boolean retrieve(int pid, int process_page){
        for(PageInfo page : pages){
            if(page.pid == pid && page.process_page == process_page)return true;
        }
        return false;
    }

    public boolean write_to(int pid, int process_page){
        for(PageInfo page : pages){
            if(page.pid == pid && page.process_page == process_page){
                page.written = true;
                return true;
            }
        }
        return false;
    }

    private class PageInfo {
        public int pid;
        public int process_page;
        public boolean allocated;
        public boolean written;

        public PageInfo(){
            pid = -1;
            process_page = -1;
            allocated = false;
            written = false;
        }
    }
}
