package com.msgclient;

import com.msgresources.MessageProtocolException;
import com.msgresources.User;
import com.msgresources.UserInterface;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;


/**
 * Simple client using Swing
 * */
public class ClientGUIMKN implements ClientGUIInterface {
    private boolean shutdown = false;
    private ClientInterface clientmkn;

    private JFrame frame = null;
    private JPanel panel = null;
    private UserTable ut = null;
    private JTextField input = null;
    private JTextPane chatbox = null;
    private volatile StyledDocument chattext = null;
    private SimpleAttributeSet self_user_style = null;
    private SimpleAttributeSet others_user_style = null;
    private SimpleAttributeSet error_style = null;
    public ClientGUIMKN(){
        frame = new JFrame("Damn Fast Messaging");
        frame.setResizable(false);
        frame.setSize(1215, 640);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosed(WindowEvent e)
                    {
                        System.out.println("windowClosed");
                        shutdown();
                        clientmkn.shutdown();
                        Runtime.getRuntime().exit(0);
                    }

                    public void windowClosing(WindowEvent e)
                    {
                        System.out.println("windowClosing");
                        shutdown();
                        clientmkn.shutdown();
                        Runtime.getRuntime().exit(0);
                    }
                });
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.cyan);
        panel.setBounds(0, 0, 1230, 600);
        //Lets add the UserTable
//Few dummy users
User[] u = new User[2];
u[0] = new User("Yolo");
u[1] = new User("Yola");
        ut = new UserTable(u, new Dimension(300, 560), ListSelectionModel.SINGLE_SELECTION);
        ut.setBounds(885, 30, 300, 560);
        ut.setVisible(true);
        ut.addMouseListener(this);
        panel.add(ut);

        //Adding chat textarea
        chatbox = new JTextPane();
        chatbox.setBounds(15, 30, 850, 530);
        chatbox.setBackground(Color.YELLOW);
        chatbox.setVisible(true);
        chattext = chatbox.getStyledDocument();
        panel.add(chatbox);
        //Adding user input textfield
        input = new JTextField();
        input.setBounds(15, 570, 850, 20);
        input.setVisible(true);
        input.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == 10){
                            System.out.println("enter");

                            String cmd = getInput();

                            //Lets call our client with data
                            try {
                                clientmkn.send(cmd);
                            } catch (MessageProtocolException ex) {
                                error(ex.getMessage());
                            }
                            // We need to catch is client is to shutdown, we do this in the GUI and not in the network component. The GUI then closes the network component, since it owns it.
                            if(getInput().trim().toLowerCase().equals("quit")){
                                System.out.println("Stop program");
                                Runtime.getRuntime().exit(0);

                            }
                            clearInput();
                        }
                        else{
                            if(input.getText().length() > 255){
                                error("Message max is 255 characters. Please shorten your message");
                            }
                        }
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                }
        );
        panel.add(input);
        //Adding panel to jframe
        frame.add(panel);
        panel.setVisible(true);
        frame.repaint();

        //Chat frame styles
        self_user_style = new SimpleAttributeSet();
        StyleConstants.setForeground(self_user_style, Color.GREEN);
        StyleConstants.setBold(self_user_style, true);
        others_user_style = new SimpleAttributeSet();
        StyleConstants.setForeground(others_user_style, Color.ORANGE);
        StyleConstants.setBold(others_user_style, true);
        error_style = new SimpleAttributeSet();
        StyleConstants.setForeground(error_style, Color.RED);
        StyleConstants.setBold(error_style, true);
        clientmkn = new ClientMKN(this);
        (new Thread(clientmkn)).start();
    }

    public void print(String str){
        System.out.println(str);
    }

    public void shutdown(){
        this.shutdown = true;
        clientmkn.shutdown();
    }

    private String welcome(){
        return "Welcome to the Chat.";
    }

    private String help(){
        return "To connect to a server write ???";
    }

    public static void main(String[] args){
        new ClientGUIMKN();
    }

    public void receivedMessage(String user, String msg){
        this.receivedMessage(user, msg, true);
    }

    @Override
    public void receivedMessage(String user, String msg, boolean self) {
        try {
            SimpleAttributeSet style;
            if(self){
                style = self_user_style;
            }
            else{
                style = others_user_style;
            }
            //Both gui and network thread can reach this point at the same time, so we need to protect the resource
            synchronized (chattext) {
                this.chattext.insertString(chattext.getLength(), user, style);
                this.chattext.insertString(chattext.getLength(), ": " + msg + "\n", null);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void error(String errmsg) {
        try {
            //Both gui and network thread can reach this point at the same time, so we need to protect the resource
            synchronized (chattext) {
                this.chattext.insertString(chattext.getLength(), errmsg + "\n", error_style);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUserList(List<UserInterface> users) {
        ut.setList(users);
    }

    @Override
    public void userSelected(UserInterface user) {
        synchronized (input) {
            this.input.setText(this.input.getText() + user.getDisplayName());
        }
    }

    private String getInput(){
        return input.getText();
    }

    private void clearInput(){
        input.setText("");
    }
}
