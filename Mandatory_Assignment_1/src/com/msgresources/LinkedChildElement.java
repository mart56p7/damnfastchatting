package com.msgresources;

//Used for FIFO
//Each parent knows its child
public class LinkedChildElement <E> {
    LinkedChildElement element = null;
    E payload = null;

    public LinkedChildElement(E payload){
        this.payload = payload;
    }

    @SuppressWarnings("unchecked")
    public LinkedChildElement add(E elementpayload){
        LinkedChildElement last = null;
        if(this.element == null){
            last = this.element = new LinkedChildElement(elementpayload);
        }
        else{
            last = this.element.add(elementpayload);
        }
        return last;
    }

    @SuppressWarnings("unchecked")
    public E get(int i){
        if(i == 0){
            return (E) payload;
        }
        if(element != null){
            return (E) element.get((i-1));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(E testpayload){
        return payload != null && payload.equals(testpayload) || element != null && element.contains(testpayload);
    }

    public LinkedChildElement getNextElement(){
        return element;
    }

    public int count(){
        if(element != null){
            return 1 + element.count();
        }
        return 1;
    }
}