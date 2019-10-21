package com.msgclient;

import com.msgresources.UserInterface;

public interface ClientGUIInterface {
    void receivedMessage(String user, String msg, boolean self);
    void error(String errmsg);
    void updateUserList(UserInterface[] users);
    void userSelected(UserInterface user);
    void shutdown();
}
