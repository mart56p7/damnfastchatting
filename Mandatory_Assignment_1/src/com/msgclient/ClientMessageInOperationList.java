package com.msgclient;

import com.msgresources.Message;
import com.msgresources.MessageProtocolException;
import com.msgresources.User;
import com.msgresources.UserInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientMessageInOperationList extends ClientMessageInOperation {

    private static final Pattern list_pattern = Pattern.compile("([a-zA-Z_0-9-]{1,12})+"); //LIST must be removed before running regex on string

    public ClientMessageInOperationList(Client client, ClientGUISwingInterface cgui){
        super(client, cgui);
    }

    @Override
    public boolean command(Message msg) throws MessageProtocolException {
        Matcher list_matcher = null;
        if (msg.getMessage().startsWith("LIST ")) {
            String input = msg.getMessage().substring(5, msg.getMessage().length());
            list_matcher = list_pattern.matcher(input);
        }
        if (list_matcher != null && list_matcher.find()) {
            List<UserInterface> users = new ArrayList<>();
            users.add(new User(list_matcher.group(1)));
            while (list_matcher.find()) {
                users.add(new User(list_matcher.group(1)));
            }
            LIST(users);
            return true;
        }
        return false;
    }

    private void LIST(List<UserInterface> users) {
        cgui.updateUserList(users);
    }
}
