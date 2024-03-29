package com.msgserver;

import com.msgresources.FIFO;
import com.msgresources.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for starting the server and related objects
 * */
public class StartServer {
    public static void main(String args[])
    {
        FIFO<Message> msgqueue = new FIFO<>();
        List<Client> soc = new ArrayList<>();
        Talker talker = new Talker(soc, msgqueue);
        Listener listener = new Listener(soc, msgqueue, talker);
        (new Thread(talker)).start();
        (new Thread(listener)).start();

        Disconnector disconnector = new Disconnector(soc, talker);
        (new Thread(disconnector)).start();

        Server server = new Server(soc, 5000);
    }
}
