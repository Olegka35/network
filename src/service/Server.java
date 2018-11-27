package service;

import service.elements.IElement;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
import service.graph.Graph;
import service.lan.LAN;
import service.lan.MyLAN;

import java.io.*;
import java.net.*;

public class Server {
    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedWriter out;
    private static BufferedReader in;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public static void main(String[] args) throws IOException {
        server = new ServerSocket(4004);
        System.out.println("Server is started!");
        clientSocket = server.accept();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        InetAddress address = Inet4Address.getByName("192.168.0.100");
        System.out.println(address.toString());

        LAN lan = new MyLAN();
        NIC nic1 = new MyNIC();
        Switch switch1 = new MySwitch();

        lan.addElement(nic1);
        lan.addElement(switch1);
        //test.addEdge("2", "1");
        // test.removeVertex("1");
        System.out.println(lan);

        String message = ois.readUTF();
        if(message.equals("getLAN")) {
            oos.writeObject(lan);
            oos.flush();
            System.out.println("LAN sent to client");
        }

        clientSocket.close();
        in.close();
        out.close();
        server.close();
    }
}
