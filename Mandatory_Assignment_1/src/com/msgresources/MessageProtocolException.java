package com.msgresources;
/**
 * Default error if an error occures when messages are exchanged
 * */
public class MessageProtocolException extends Exception{
    public MessageProtocolException(String str){
        super(str);
    }
}