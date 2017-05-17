/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import protocol.Message;
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
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;
    private String name;
    private JLabel countLabel;
    
    public Client(String name) {
        this.name = name;
        init();
        try {
            socket = new Socket("localhost", 55555);
            System.out.println("connected to " + socket);
            System.out.println("connected as " + name);
            chatTextArea.setText("Vítejte v chatovací místnosti!\n\n");
            objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(new Message(name, "", " se připojil"));
            System.out.println("Sending welcome message");
            objectInput = new ObjectInputStream(socket.getInputStream());
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
                Message message = (Message)objectInput.readObject();
                chatTextArea.append(message.getName() + message.getWelcome() + " ("
                        + dateFormat.format(date) + ")\n" + message.getMessage() + "\n");
                countLabel.setText("Počet uživatelů v místnosti: "
                        + Integer.toString(message.getUserCount()));
                System.out.println("Receiving message");
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
        JLabel nameLabel = new JLabel("Přihlášen jako: " + this.name);
        chatTextArea.setEditable(false);
        JPanel panel = new JPanel();
        JPanel panel1 = new JPanel();
        this.setContentPane(panel);
        this.setSize(600, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setLayout(new BorderLayout());
        panel.add("North", chatTextField);
        panel.add("Center", chatTextArea);
        panel1.setLayout(new BorderLayout());
        panel1.add("West", nameLabel);
        panel1.add("East", countLabel);
        panel.add("South", panel1);
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
            objectOutput.writeObject(new Message(name, chatTextField.getText() + "\n", ""));
            chatTextField.setText("");
            System.out.println("Sending message");
        }
        catch(IOException ie) {
            System.out.println(ie);
        }
    }
}
