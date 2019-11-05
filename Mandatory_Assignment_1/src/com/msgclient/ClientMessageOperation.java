package com.msgclient;

import com.msgclient.Client;
import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.io.IOException;

public abstract class ClientMessageOperation {
    protected Client client;
    protected ClientGUISwingInterface cgui;

    public ClientMessageOperation(Client client, ClientGUISwingInterface cgui){
        this.client = client;
        this.cgui = cgui;
    }
    /*
    * Returns true if the command is accepted
    * Returns false if the command is accepted
    * */
    public abstract boolean command(Message msg) throws MessageProtocolException;

    protected void disconnected(){
        if(client != null){
            if(client.isConnected()) {
                cgui.receivedMessage("System", "Disconnected from server. Closing connection", true);
            }
            client.destroy();
        }
    }
}
