package com.msgresources;

public interface FIFOObserver{
    /**
     * Method is called, when the an empty FIFO queue is populated by an element.
     * */
    void FIFONotEmpty();
}