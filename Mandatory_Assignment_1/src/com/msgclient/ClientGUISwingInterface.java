package com.msgclient;

import com.msgresources.UserInterface;

import java.util.List;

public interface ClientGUISwingInterface {
    void receivedMessage(String user, String msg, boolean self);
    void error(String errmsg);
    void updateUserList(List<UserInterface> users);
    void userSelected(UserInterface user);
    void shutdown();
    void setClientNetworkInterface(ClientNetworkInterface cni);
}
