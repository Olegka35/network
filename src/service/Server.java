package service;

import service.elements.IElement;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.router.MyRouter;
import service.elements.router.Router;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
import service.graph.Graph;
import service.ip.IP;
import service.lan.LAN;
import service.lan.MyLAN;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 4004;
    public static LinkedList<ServerThread> serverList = new LinkedList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("Server is started!");

        LAN lan = new MyLAN();
        Router router = new MyRouter(5);
        lan.addElement(router);
        router.configurePort(0, new IP("215.70.1.7"));
        router.configurePort(1, new IP("192.168.0.2"));

        NIC nic1 = new MyNIC();
        NIC nic2 = new MyNIC();
        Switch switch1 = new MySwitch();
        lan.addElement(nic1);
        lan.addElement(nic2);
        lan.addElement(switch1);
        switch1.configureSwitch(new IP("192.161.0.1"), 24);
        System.out.println(router.checkConnectAbility(switch1));
        //lan.connectTwoElements(nic1, switch1);
        //lan.connectTwoElements(nic1, nic2);

        ServerSocket server = new ServerSocket(PORT);
        try {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerThread(socket, lan));
                } catch (IOException e) {
                    socket.close();
                }
            }
        } finally {
            server.close();
        }



        /*LAN lan = new MyLAN();
        NIC nic1 = new MyNIC();
        NIC nic2 = new MyNIC();
        Switch switch1 = new MySwitch();

        IP ip1 = new IP("192.168.32.123");
        System.out.println(ip1.getNetIpByMask(32));

        lan.addElement(nic1);
        lan.addElement(nic2);
        lan.addElement(switch1);
        lan.connectTwoElements(nic1, switch1);
        lan.connectTwoElements(nic1, nic2);
        System.out.println(lan);



        clientSocket = server.accept();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        String message = ois.readUTF();
        if(message.equals("getLAN")) {
            oos.writeObject(lan);
            oos.flush();
            System.out.println("LAN sent to client");
        }

        clientSocket.close();
        in.close();
        out.close();
        server.close();*/
    }
}
