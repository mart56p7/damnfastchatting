package com.msgclient;

import com.msgresources.UserInterface;

import java.util.List;


/**
 * Interface to our Mediator between GUI and logic
 * */
public interface ClientGUISwingInterface {
    /**
     * Called to display text in the GUI Client
     * */
    void receivedMessage(String user, String msg, boolean self);

    /**
     * Called to display errors to the GUI Client
     * */
    void error(String errmsg);
    /**
     * Called to update the User List
     * */
    void updateUserList(List<UserInterface> users);
    void userSelected(UserInterface user);
    void shutdown();
    void setClientNetworkInterface(ClientNetworkInterface cni);
}
