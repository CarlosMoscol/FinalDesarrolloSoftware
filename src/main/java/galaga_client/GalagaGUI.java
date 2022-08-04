package galaga_client;
import galaga_game.Galaga;
import galaga_server.GalagaServer;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GalagaGUI extends JFrame {
    //String IP = "192.168.0.27";
    //int PORT = 4444;

    public Galaga galaga = null;

    JPanel connectionPanel = new JPanel();
    JLabel ipAddressLabel = new JLabel("IP: ");
    JTextField ipAddresTextField = new JTextField();
    JLabel portLabel = new JLabel("PORT: ");
    JTextField portTextField = new JTextField();
    JPanel startPanel = new JPanel();
    JButton createServerButton = new JButton("Create Server");
    JButton joinGameButton = new JButton("Join Game");
    JButton startGameButton = new JButton("Start");
    JButton exitGameButton = new JButton("Exit");
    JButton closeServerButton = new JButton("Close Server");
    JPanel boardPanel = new JPanel();
    JTextPane boardTextPane = new JTextPane();//720, 540
    JTextPane messageTextPane = new JTextPane();

    public GalagaGUI(Galaga galaga) {
        this.galaga = galaga;
        //Send the keyCode of the key pressed
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (galaga != null && galaga.tcpClient != null && galaga.status == 1) galaga.tcpClient.sendMessage("key " + galaga.id + " " + keyCode);
            }
        });

        setTitle("Galaga");
        setPreferredSize(new Dimension(800, 700));
        setSize(800, 700);
        //setResizable(false);
        setLayout(new BorderLayout(20, 20));

        setConnectionPanel();
        setBoardPanel();

        createServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String IP = ipAddresTextField.getText();
                int PORT = Integer.parseInt(portTextField.getText());
                try {
                    new Thread(new GalagaServer(PORT)).start();
                    Thread.sleep(50);   //Delay entre crear el servidor y conectarse a el
                    galaga.startClient(IP, PORT);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                waitRoomMaster();
            }
        });

        joinGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String IP = ipAddresTextField.getText();
                int PORT = Integer.parseInt(portTextField.getText());
                try {
                    galaga.startClient(IP, PORT);
                    Thread.sleep(20);//
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                waitRoomSlave();
            }
        });

        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        exitGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                galaga.tcpClient.sendMessage("leave " + galaga.id);
                setAllFocusable(true);
                homeRoom();
            }
        });

        closeServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                galaga.tcpClient.stopClient();
                homeRoom();
            }
        });

        startPanel.add(createServerButton);
        startPanel.add(joinGameButton);

        this.getContentPane().add(connectionPanel, BorderLayout.PAGE_START);
        this.getContentPane().add(boardPanel, BorderLayout.CENTER);
        this.getContentPane().add(startPanel, BorderLayout.PAGE_END);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setConnectionPanel() {
        Font font = new Font("Arial", Font.BOLD, 16);
        ipAddresTextField.setPreferredSize(new Dimension(170, 30));
        ipAddresTextField.setFont(font);
        ipAddresTextField.setHorizontalAlignment(JTextField.CENTER);
        portTextField.setPreferredSize(new Dimension(60, 30));
        portTextField.setFont(font);
        portTextField.setHorizontalAlignment(JTextField.CENTER);
        connectionPanel.add(ipAddressLabel);
        connectionPanel.add(ipAddresTextField);
        connectionPanel.add(portLabel);
        connectionPanel.add(portTextField);
    }
    //
}