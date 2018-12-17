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
        Router router = new MyRouter(5, "Router1");
        lan.addElement(router);
        router.configurePort(0, new IP("215.70.1.7"), 24);
        router.configurePort(1, new IP("192.168.0.2"), 24);
        router.configurePort(2, new IP("192.168.1.0"), 24);
        router.configurePort(3, new IP("192.168.2.0"), 24);
        router.configurePort(4, new IP("192.168.3.0"), 24);

        NIC nic1 = new MyNIC("NIC1");
        NIC nic2 = new MyNIC("NIC2");
        Switch switch1 = new MySwitch(3, "FirstSwitch");
        lan.addElement(nic1);
        lan.addElement(nic2);
        nic1.configureNIC(new IP("215.70.1.1"), 24);
        nic2.configureNIC(new IP("192.168.0.1"), 24);
        lan.addElement(switch1);
        //System.out.println(lan.checkConnectAbility(nic1, router));
        System.out.println(lan.checkConnectAbility(nic2, router));
        System.out.println(lan.checkConnectAbility(nic1, switch1));
        lan.connectTwoElements(nic1, switch1);
        lan.connectTwoElements(nic2, router);
        lan.connectTwoElements(router, switch1);

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
    }
}
