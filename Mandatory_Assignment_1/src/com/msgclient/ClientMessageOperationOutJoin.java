package com.msgclient;

import com.msgresources.Message;
import com.msgresources.User;
import com.msgresources.MessageProtocolException;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationOutJoin extends ClientMessageOperation {
    private static final Pattern join_pattern = Pattern.compile("\\AJOIN ([a-zA-Z_0-9-]{1,12}), ((\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})):(\\d{1,65535})\\Z");

    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationOutJoin(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a Join message
     * @return is the msg is a Join message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher join_matcher = join_pattern.matcher(msg.getMessage());
        if(join_matcher.find()){
            //If we are already connected to a server, disconnect
            if(client.isConnected()){
                try {
                    QUIT();
                } catch (MessageProtocolException e) {
                    e.printStackTrace();
                }
                cgui.receivedMessage("System", "Closing connection to server..", true);
            }
            //Tell our user that we are connecting
            cgui.receivedMessage("System", "Connecting to server " + join_matcher.group(2) + " on port " + Integer.parseInt(join_matcher.group(7)), true);
            //If any errors occur, we disconnect
            JOIN(join_matcher.group(1), join_matcher.group(2), Integer.parseInt(join_matcher.group(7)));
            return true;
        }
        return false;
    }

    //Helper function to connect to the server
    private void JOIN(String username, String server, int port) throws MessageProtocolException {
        try {
            //If we fail to setup a socket, then we ignore everything else
            this.client.setSocket(new Socket(server, port));
            //We have already in command checked that the username is of a valid format
            this.client.setUser(new User(username));
            this.client.sendMessage("JOIN " + username + ", " + server + ":" + port);
        } catch (IOException e) {
            cgui.error("Failed to connect to " + server + ":" + port + " with message: " + e.getMessage());
            throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + e.getMessage());
        } catch (MessageProtocolException m){
            QUIT();
            cgui.error("Failed to connect to " + server + ":" + port + " with message: " + m.getMessage());
            throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + m.getMessage());
        }
    }

    //Helper function to disconnect from a server
    private void QUIT() throws MessageProtocolException {
        this.client.sendMessage("QUIT");
    }
}
