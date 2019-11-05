package com.msgclient;

import com.msgresources.*;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Simple client using Swing
 * */
public class ClientGUISwing implements ClientGUISwingInterface {
    private boolean shutdown = false;
    private ClientNetworkInterface clientmkn;

    private JFrame frame = null;
    private JPanel panel = null;
    private UserTableSwing ut = null;
    private JTextField input = null;
    private JTextPane chatbox = null;
    private JScrollPane chatboxscroll = null;
    private volatile StyledDocument chattext = null;
    private SimpleAttributeSet self_user_style = null;
    private SimpleAttributeSet others_user_style = null;
    private SimpleAttributeSet error_style = null;

    //Doskey - just going to min/max not going in a ring!
    private List<String> inputqueue = null;
    private int inputposition = 0;

    public ClientGUISwing(){
        inputqueue = new ArrayList<>();
        //Easy testing
        inputqueue.add("JOIN ImAwesome, 127.0.0.1:5000");
        inputposition++;

        frame = new JFrame("Damn Fast Messaging");
        frame.setResizable(false);
        frame.setSize(900, 640);
        frame.setLayout(new FlowLayout());
        frame.setVisible(true);
        frame.setBackground(Color.white);
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
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(900, 600));
        //panel.setBounds(0, 0, 1230, 600);
        panel.setBackground(Color.white);

        //Adding chat textarea
        chatbox = new JTextPane();
        //chatbox.setBackground(Color.YELLOW);
        chatbox.setVisible(true);
        chattext = chatbox.getStyledDocument();
        chatbox.setEditable(false);
        chatboxscroll = new JScrollPane(chatbox);
        chatboxscroll.setPreferredSize(new Dimension(685, 560));

        //Ref: https://stackoverflow.com/questions/4045722/how-to-make-jtextpane-autoscroll-only-when-scroll-bar-is-at-bottom-and-scroll-lo/4047794#4047794
        chatboxscroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            BoundedRangeModel brm = chatboxscroll.getVerticalScrollBar().getModel();
            boolean wasAtBottom = true;

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!brm.getValueIsAdjusting()) {
                    if (wasAtBottom)
                        brm.setValue(brm.getMaximum());
                } else
                    wasAtBottom = ((brm.getValue() + brm.getExtent()) == brm.getMaximum());

            }
        });
        panel.add(chatboxscroll);

        //Lets add the user table
        ut = new UserTableSwing(null, new Dimension(200, 560), ListSelectionModel.SINGLE_SELECTION);
        ut.setPreferredSize(new Dimension(200, 560));
        //ut.setBounds(885, 30, 300, 560);
        ut.setVisible(true);
        ut.addMouseListener(this);
        panel.add(ut);

        //Adding user input textfield
        input = new JTextField();
        input.setPreferredSize(new Dimension(frame.getWidth()-10, 20));
        //input.setBounds(15, 570, 850, 20);
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
                            inputqueue.add(cmd);
                            inputposition = inputqueue.size();
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
                            //38 arrow up - get previous msg
                            //40 Arrow down
                            if(e.getKeyCode() == 38 || e.getKeyCode() == 40) {
                                if (e.getKeyCode() == 38) {
                                    inputposition = Math.max(inputposition - 1, 0);
                                }
                                if (e.getKeyCode() == 40) {
                                    inputposition = Math.min(inputposition + 1, inputqueue.size()-1);
                                }
                                input.setText(inputqueue.get(inputposition));
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
        frame.setVisible(true);

        //Chat frame styles
        self_user_style = new SimpleAttributeSet();
        StyleConstants.setForeground(self_user_style, Color.BLUE);
        StyleConstants.setBold(self_user_style, true);
        others_user_style = new SimpleAttributeSet();
        StyleConstants.setForeground(others_user_style, Color.BLACK);
        StyleConstants.setBold(others_user_style, true);
        error_style = new SimpleAttributeSet();
        StyleConstants.setForeground(error_style, Color.RED);
        StyleConstants.setBold(error_style, true);

        //Setting up application layer in network
        Client client = new Client();
        ClientMessageOperation[] cmo = new ClientMessageOperation[1];
        cmo[0] = new ClientMessageOperationOutJoin(client, this);
        ClientMessageController cmoc = new ClientMessageController(cmo);

        ClientMessageOperation[] cmi = new ClientMessageOperation[2];
        cmi[0] = new ClientMessageOperationInJ_OK(client, this);
        cmi[1] = new ClientMessageOperationInList(client, this);
        ClientMessageController cmic = new ClientMessageController(cmo);

        clientmkn = new ClientNetwork(this,
                                            new Client(),
                                            cmic,
                                            cmoc);
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
