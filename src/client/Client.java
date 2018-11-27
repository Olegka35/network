package client;

import service.elements.IElement;
import service.elements.nic.NIC;
import service.lan.LAN;
import service.lan.MyLAN;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        clientSocket = new Socket("localhost", 4004);
        reader = new BufferedReader(new InputStreamReader(System.in));
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());

        System.out.println("Print message:");
        String message = reader.readLine();
        oos.writeUTF(message);
        oos.flush();

        if(message.equals("getLAN")) {
            LAN lan = (LAN) ois.readObject();
            System.out.println("LAN: " + lan);
        }

        clientSocket.close();
        reader.close();
        in.close();
        out.close();
    }
}
