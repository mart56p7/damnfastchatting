package com.msgresources;

public interface FIFOInterface <E> {
    void push(E e);
    E pop();
    E peek();
    int size();
    void grow();
}