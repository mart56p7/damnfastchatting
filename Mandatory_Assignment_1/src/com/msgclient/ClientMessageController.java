package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

// https://www.gofpatterns.com/behavioral-design-patterns/behavioral-patterns/chain-of-responsibility.php
// Implementering af chain of responsibility pattern.

/**
 * A message controller. Tried to match a Message against a operation. When the right operation is found it is executed.
 * */
public class ClientMessageController {
    ClientMessageOperation[] msgoperations;

    /**
     * @param msgoperations Operations the message controller is handling.
     * */
    public ClientMessageController(ClientMessageOperation[] msgoperations){
        this.msgoperations = msgoperations;
    }

    /**
     * @param cmd The Message that the ClientMessageController is checking upagainst all of the ClientMessageOperation's the ClientMessageController is handling
     * @return true if a ClientMessageOperation is found to handle the cmd, else returns false.
     */
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
