/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Date;
import javax.swing.JLabel;

/**
 *
 * @author Jircus
 */
public class Client extends JFrame implements Runnable {
    private TextField chatTextField;
    private TextArea chatTextArea;    
    private Socket socket;
    private ObjectOutputStream dout;
    private ObjectInputStream din;
    private String name;
    private JLabel countLabel;
    
    public Client(String name) {
        this.name = name;
        init();
        try {
            socket = new Socket("localhost", 55555);
            System.out.println("connected to " + socket);
            chatTextArea.setText("Vítejte v chatovací místnosti!\n\n");
            dout = new ObjectOutputStream(socket.getOutputStream());
            dout.writeObject(new Message("Uživatel " + name + " se připojil", ""));
            din = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
        }
        catch(IOException ie) {
            System.out.println(ie);
            chatTextArea.setText("Server nebyl nalazen");
        }
    }
    
    @Override
    public void run() {
        try {
            while (true) {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                Date date = new Date();
                Message message = (Message)din.readObject();
                chatTextArea.append(message.getName() + " (" + dateFormat.format(date)
                    + ")\n" + message.getMessage() + "\n");
                countLabel.setText(Integer.toString(message.getUserCount()));
            }
        }
        catch(IOException ie) {
            System.out.println(ie);
        }
        catch (ClassNotFoundException ex) {
            System.out.println(ex);
        } 
    }
    
    public void init() {
        chatTextField = new TextField();
        chatTextArea = new TextArea();
        countLabel = new JLabel("");
        chatTextArea.setEditable(false);
        JPanel panel = new JPanel();
        this.setContentPane(panel);
        this.setSize(600, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setLayout(new BorderLayout());
        panel.add("North", chatTextField);
        panel.add("Center", chatTextArea);
        panel.add("South", countLabel);
        chatTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterPressed(evt);
                }
            }
        });
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    private void enterPressed(KeyEvent evt) {
        try {
            dout.writeObject(new Message(name, chatTextField.getText() + "\n"));
            chatTextField.setText("");
        }
        catch(IOException ie) {
            System.out.println(ie);
        }
    }
}
