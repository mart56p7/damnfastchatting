package com.msgclient;

import com.msgresources.MessageProtocolException;

public interface ClientNetworkInterface extends Runnable {
    void send(String msg) throws MessageProtocolException;
    void shutdown();
}
