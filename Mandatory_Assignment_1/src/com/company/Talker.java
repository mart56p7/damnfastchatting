package com.company;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Talker implements Runnable {

    private List<Socket> sockets = new ArrayList<>();

    public Talker(List<Socket> sockets){
        this.sockets = sockets;
    }

    @Override
    public void run() {

    }
}
