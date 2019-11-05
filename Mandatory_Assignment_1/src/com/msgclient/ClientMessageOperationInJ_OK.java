package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

public class ClientMessageOperationInJ_OK extends ClientMessageOperation {

    public ClientMessageOperationInJ_OK(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        if (msg.getMessage().equals("J_OK")) {
            try {
                System.out.println("Received OK");
                J_OK();
            } catch (MessageProtocolException e) {
                throw new MessageProtocolException("Failed to finish handshake with server error message " + e.getMessage());
            }
            return true;
        }
        return false;
    }

    public void J_OK() throws MessageProtocolException {
        if(client.getUser() != null && client.getSocket() != null && client.isConnected() == false){
            client.setConnected(true);
            System.out.println("J_OK " + client.isConnected());
            cgui.receivedMessage("System", "You are now connected to " + client.getSocket().getInetAddress() + " on port " + client.getSocket().getPort(), true);
        }
    }
}
