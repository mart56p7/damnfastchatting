package com.msgclient;

/**
 * This class is used for starting the client.
 * */
public class StartClient {
    public static void main(String[] args){
        ClientGUISwingInterface cgui = new ClientGUISwing();

        //Setting up application layer in network
        Client client = new Client();
        ClientMessageOperation[] cmo = new ClientMessageOperation[3];
        cmo[0] = new ClientMessageOperationOutJoin(client, cgui);
        cmo[1] = new ClientMessageOperationOutMsg(client, cgui);
        cmo[2] = new ClientMessageOperationOutHelp(client, cgui);
        ClientMessageController cmoc = new ClientMessageController(cmo);

        ClientMessageOperation[] cmi = new ClientMessageOperation[4];
        cmi[0] = new ClientMessageOperationInJ_OK(client, cgui);
        cmi[1] = new ClientMessageOperationInList(client, cgui);
        cmi[2] = new ClientMessageOperationInMsg(client, cgui);
        cmi[3] = new ClientMessageOperationInJ_ER(client, cgui);
        ClientMessageController cmic = new ClientMessageController(cmi);

        ClientNetworkInterface cni = new ClientNetwork(cgui,
                                                        client,
                                                        cmic,
                                                        cmoc);
        //Adding so gui can talk to network application layer
        cgui.setClientNetworkInterface(cni);
        //Start the network application layer
        (new Thread(cni)).start();


    }
}
