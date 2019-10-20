package com.msgclient;

import com.msgresources.MessageProtocolException;

public interface ClientInterface extends Runnable {
    public void send(String msg) throws MessageProtocolException;
    public void shutdown();
}
