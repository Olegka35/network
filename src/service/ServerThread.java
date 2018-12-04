package service;

import service.lan.LAN;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket socket;
    private LAN lan;

    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public ServerThread(Socket socket, LAN lan) throws IOException {
        this.socket = socket;
        this.lan = lan;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = ois.readUTF();
                if (message.equals("getLAN")) {
                    oos.writeObject(lan);
                    oos.flush();
                    System.out.println("LAN sent to client");
                }
            }
        } catch (Exception e) {
            System.out.println("Client closed the connection");
        }
    }
}
