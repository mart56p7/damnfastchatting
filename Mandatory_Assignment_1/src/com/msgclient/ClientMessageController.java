package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

public class ClientMessageController {
    ClientMessageOperation[] msgoperations;

    public ClientMessageController(ClientMessageOperation[] msgoperations){
        this.msgoperations = msgoperations;
    }
    /*
     * Returns true if the command is accepted
     * Returns false if the command is accepted
     * */
    public boolean command(Message cmd){
        boolean result = false;
        try {

            for (ClientMessageOperation msgoperation : msgoperations) {
                result = result || msgoperation.command(cmd);
            }
        }
        catch(MessageProtocolException mpe){
            System.out.println("ClientMessageController " + mpe.getMessage());
        }
        return result;
    }
}
