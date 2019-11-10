package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;

public class ClientMessageOperationOutHelp extends ClientMessageOperation {
    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationOutHelp(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a Help message
     * @return is the msg is a Help message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        if (msg.getMessage().equalsIgnoreCase("help")) {
            help();
            return true;
        }
        return false;
    }


    /*
     * Internal helper function. Prints out available commands
     * */
    private void help(){
        this.cgui.receivedMessage("System", "", true);
        this.cgui.receivedMessage("System", "Welcome to the chat system.", true);
        this.cgui.receivedMessage("System", "Commands", true);
        this.cgui.receivedMessage("System", "JOIN <<user_name>>, <<server_ip>>:<<server_port>>", true);
        this.cgui.receivedMessage("System", "- After having connected to a server, type any none command key words to send to the chat server", true);
        this.cgui.receivedMessage("System", "QUIT", true);
        this.cgui.receivedMessage("System", "HELP", true);
        this.cgui.receivedMessage("System", "", true);
        this.cgui.receivedMessage("System", "Examples", true);
        this.cgui.receivedMessage("System", "Example join server:", true);
        this.cgui.receivedMessage("System", "JOIN ImAwesome, 127.0.0.1:5000", true);
        this.cgui.receivedMessage("System", "", true);
        this.cgui.receivedMessage("System", "Example close connection to server and quit program", true);
        this.cgui.receivedMessage("System", "QUIT", true);
        this.cgui.receivedMessage("System", "", true);
        this.cgui.receivedMessage("System", "Show this menu", true);
        this.cgui.receivedMessage("System", "HELP", true);
        this.cgui.receivedMessage("System", "", true);
    }
}
