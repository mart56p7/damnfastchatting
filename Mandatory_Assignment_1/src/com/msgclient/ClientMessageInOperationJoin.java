package com.msgclient;

import com.msgresources.Message;
import com.msgresources.User;
import com.msgresources.MessageProtocolException;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageInOperationJoin extends ClientMessageInOperation {
    private static final Pattern join_pattern = Pattern.compile("\\AJOIN ([a-zA-Z_0-9-]{1,12}), ((\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})):(\\d{1,65535})\\Z");

    public ClientMessageInOperationJoin(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher join_matcher = join_pattern.matcher(msg.getMessage());
        if(join_matcher.find()){
            if(client.isConnected()){
                try {
                    QUIT();
                } catch (MessageProtocolException e) {
                    e.printStackTrace();
                }
                cgui.receivedMessage("System", "Closing connection to server..", true);
            }

            cgui.receivedMessage("System", "Connecting to server " + join_matcher.group(2) + " on port " + Integer.parseInt(join_matcher.group(7)), true);
            JOIN(join_matcher.group(1), join_matcher.group(2), Integer.parseInt(join_matcher.group(7)));
            return true;
        }
        return false;
    }

    public void JOIN(String username, String server, int port) throws MessageProtocolException {
        try {
            this.client.setSocket(new Socket(server, port));
            serverSend("JOIN " + username + ", " + server + ":" + port);
            this.client.setUser(new User(username));
        } catch (IOException e) {
            throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + e.getMessage());
        } catch (MessageProtocolException m){
            throw new MessageProtocolException("Failed to connect to " + server + ":" + port + " with message: " + m.getMessage());
        }
    }

    public void QUIT() throws MessageProtocolException {
        serverSend("QUIT");
    }
}
