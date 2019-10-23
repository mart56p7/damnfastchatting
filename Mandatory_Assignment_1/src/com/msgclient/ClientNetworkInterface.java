package com.msgclient;

import com.msgresources.MessageProtocolException;

public interface ClientNetworkInterface extends Runnable {
    public void send(String msg) throws MessageProtocolException;
    public void shutdown();
}
