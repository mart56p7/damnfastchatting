package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;
import com.msgresources.User;
import com.msgresources.UserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageOperationInList extends ClientMessageOperation {

    private static final Pattern list_pattern = Pattern.compile("([a-zA-Z_0-9-]{1,12})+"); //LIST must be removed before running regex on string

    /**
     * @param client The client data
     * @param cgui Mediator to GUI components
     * */
    public ClientMessageOperationInList(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    /**
     * @param msg The Message that is being checked if its a LIST message
     * @return is the msg is a LIST message returns true, else returns false
     * */
    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher list_matcher = null;
        //All list messages starts with LIST
        if (msg.getMessage().startsWith("LIST ")) {
            //If its a list message, we remove the first 'LIST '
            String input = msg.getMessage().substring(5, msg.getMessage().length());
            //Check if it matches the regex
            list_matcher = list_pattern.matcher(input);
        }
        //If the message has started with 'LIST ' and we got a regex match procede
        if (list_matcher != null && list_matcher.find()) {
            //Extract all usernames
            List<UserInterface> users = new ArrayList<>();
            users.add(new User(list_matcher.group(1)));
            while (list_matcher.find()) {
                users.add(new User(list_matcher.group(1)));
            }
            //Call our helper function with all the user names
            LIST(users);
            //We have received a LIST message so returning true
            return true;
        }
        //Not a list message or the message format after 'LIST ' is not allowed
        return false;
    }

    /*
     * Internal helper function. Calls the cgui so the client user can see users connected to chat server
     * */
    private void LIST(List<UserInterface> users) {
        cgui.updateUserList(users);
    }
}
