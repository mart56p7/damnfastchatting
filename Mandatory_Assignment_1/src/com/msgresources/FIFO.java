package com.msgresources;

import java.util.ArrayList;
import java.util.List;

// FIFO (First In First Out) Queue
// This queue is thread safe
public class FIFO <E> implements FIFOInterface <E>{
   private volatile LinkedChildElement first = null;
   private volatile LinkedChildElement last = null;
   private volatile List<FIFOObserver> observers = new ArrayList<FIFOObserver>();


   @SuppressWarnings("unchecked")
   public void push(E e){
      synchronized (this) {
         if (first == null) {
            last = new LinkedChildElement(e);
            first = last;
            synchronized (observers) { // This is kinda implied but just to make 1000% sure
               for (FIFOObserver observer : observers) {
                  observer.FIFONotEmpty();
               }
            }
         } else {
            last = last.add(e);
         }
      }
   }
   
   @SuppressWarnings("unchecked")
   public E pop(){
      synchronized (this) {
         if (first == null) {
            return null;
         }
         E tmpfirst = (E) this.first.get(0);
         this.first = this.first.getNextElement();
         return (E) tmpfirst;
      }
   }
   
   @SuppressWarnings("unchecked")
   public E peek(){
      synchronized (this) {
         if (this.first == null) {
            return null;
         }
         return (E) this.first.get(0);
      }
   }
   
   public int size(){
      synchronized (this) {
         if (first == null) {
            return 0;
         }
         return this.first.count();
      }
   }
   
   public void grow(){
      //Does nothing
   }

   public E get(int i) {
      synchronized (this) {
         if (first == null || i < 0) {
            return null;
         }
         return (E) first.get(i);
      }
   }

   public void attach(FIFOObserver observer){
      synchronized (observer) {
         observers.add(observer);
      }
   }

   public void deattach(FIFOObserver observer){
      synchronized (observer) {
         observers.remove(observer);
      }
   }
}