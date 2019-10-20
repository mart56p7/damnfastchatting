package com.msgclient;

import java.util.List;

public interface ClientGUIInterface {
    void receivedMessage(String user, String msg, boolean self);
    void error(String errmsg);
    void updateUserList(List<String> users);
    void userSelected(UserInterface user);
}
