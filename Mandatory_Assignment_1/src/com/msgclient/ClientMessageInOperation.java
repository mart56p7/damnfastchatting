package com.msgclient;

import com.msgclient.Client;
import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

import java.io.IOException;

public abstract class ClientMessageInOperation {
    protected Client client;
    protected ClientGUISwingInterface cgui;

    public ClientMessageInOperation(Client client, ClientGUISwingInterface cgui){
        this.client = client;
        this.cgui = cgui;
    }
    /*
    * Returns true if the command is accepted
    * Returns false if the command is accepted
    * */
    public abstract boolean command(Message msg) throws MessageProtocolException;

    protected void serverSend(String msg) throws MessageProtocolException {
        if(client.isConnected()){
            try {
                System.out.println("Sending " + msg);
                client.getDataOutputStream().writeUTF(msg);
            } catch (IOException e) {
                new MessageProtocolException(e.getMessage());
                disconnected();
            }
        }
    }

    protected void disconnected(){
        if(client != null){
            if(client.isConnected()) {
                cgui.receivedMessage("System", "Disconnected from server. Closing connection", true);
            }
            client.destroy();
        }
    }
}
