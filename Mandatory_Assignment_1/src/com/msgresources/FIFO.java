package com.msgresources;

import java.util.ArrayList;
import java.util.List;

/**
 * FIFO (First In First Out) Queue
 * This FIFO queue is thread safe
 */
public class FIFO <E> implements StackInterface <E>{
   private volatile LinkedChildElement first = null;
   private volatile LinkedChildElement last = null;
   private volatile List<FIFOObserver> observers = new ArrayList<FIFOObserver>();


   @Override
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

   @Override
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

   @Override
   @SuppressWarnings("unchecked")
   public E peek(){
      synchronized (this) {
         if (this.first == null) {
            return null;
         }
         return (E) this.first.get(0);
      }
   }

   @Override
   public int size(){
      synchronized (this) {
         if (first == null) {
            return 0;
         }
         return this.first.count();
      }
   }

   @Override
   public void grow(){
      //Does nothing
   }

   /**
    * @param i gets the i'th element in the queue
    * */
   public E get(int i) {
      synchronized (this) {
         if (first == null || i < 0) {
            return null;
         }
         return (E) first.get(i);
      }
   }

   /**
    * @param observer add the observer to the FIFO queue, when new messages are pushed onto an empty queue, observers are notified.
    * */
   public void attach(FIFOObserver observer){
      synchronized (observer) {
         observers.add(observer);
      }
   }

   /**
    * @param observer remove the given observer
    * */
   public void deattach(FIFOObserver observer){
      synchronized (observer) {
         observers.remove(observer);
      }
   }
}