package com.msgresources;

/**
 * Used for FIFO
 * Each parent knows its child. This design is specifically made for FIFO.
 */
public class LinkedChildElement <E> {
    LinkedChildElement element = null;
    E payload = null;

    /**
     * @param payload the data element that the LinkedChildElement contains
     * */
    public LinkedChildElement(E payload){
        this.payload = payload;
    }

    /**
     * @param elementpayload adds the elementpayload as a LinkedChildElement to the LinkedChildElement object. If the current LinkedChildElement already has a LinkedChildElement then the LinkedChildElement is called with the elementpayload.
     * */
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

    /**
     * @param i gets the i'th element.
     * */
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

    /**
     * @param testpayload test if the element is containted in the link of LinkedChildElement's
     * */
    @SuppressWarnings("unchecked")
    public boolean contains(E testpayload){
        return payload != null && payload.equals(testpayload) || element != null && element.contains(testpayload);
    }

    /**
     * @return the parent element.
     * */
    public LinkedChildElement getNextElement(){
        return element;
    }

    /**
     * @return the amount of elements that is below the current elemtn
     */
    public int count(){
        if(element != null){
            return 1 + element.count();
        }
        return 1;
    }
}