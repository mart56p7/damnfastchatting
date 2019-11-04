package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

public class ClientMessageInController {
    ClientMessageInOperation[] msgoperations;

    public ClientMessageInController(ClientMessageInOperation[] msgoperations){
        this.msgoperations = msgoperations;
    }
    /*
     * Returns true if the command is accepted
     * Returns false if the command is accepted
     * */
    public boolean command(Message cmd){
        boolean result = false;
        try {

            for (ClientMessageInOperation msgoperation : msgoperations) {
                result = result || msgoperation.command(cmd);
            }
        }
        catch(MessageProtocolException mpe){
            System.out.println("ClientMessageInController " + mpe.getMessage());
        }
        return result;
    }
}
