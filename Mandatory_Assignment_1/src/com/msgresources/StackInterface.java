package com.msgresources;

/**
 * Default stack interface
 * */
public interface StackInterface <E> {
    /**
     * @param e insert the element as the last element of the FIFO queue
     * If the FIFO queue is empty, all observers are notified, in their FIFONotEmpty method.
     * */
    void push(E e);
    /**
     * @return the first element in the FIFO queue, and removes it from the queue
     * */
    E pop();
    /**
     * @return returns the first element of the FIFO queue, with out popping it
     * */
    E peek();
    /**
     * @return the size of the FIFO queue
     * */
    int size();
    /**
     * @Depricated
     * */
    void grow();
}