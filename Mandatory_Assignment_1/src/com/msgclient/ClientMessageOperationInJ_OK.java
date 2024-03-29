package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

public class ClientMessageOperationInJ_OK extends ClientMessageOperation {
    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationInJ_OK(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a J_OK message
     * @return is the msg is a J_OK message returns true, else returns false
     * */
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


    /*
    * Internal helper function. Calls the cgui so the client user can see that a connection is made to the server
    * */
    private void J_OK() throws MessageProtocolException {
        System.out.println(client.getUser() + " -- " + client.getSocket() + " -- " + client.isConnected());
        if(client.getUser() != null && client.getSocket() != null && client.isConnected()){
            System.out.println("J_OK " + client.isConnected());
            cgui.receivedMessage("System", "You are now connected to " + client.getSocket().getInetAddress() + " on port " + client.getSocket().getPort(), true);
        }
    }
}
