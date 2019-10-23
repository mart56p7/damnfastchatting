package com.msgresources;

public interface StackInterface <E> {
    void push(E e);
    E pop();
    E peek();
    int size();
    void grow();
}