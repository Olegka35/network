package service;

import client.message.MES_TYPE;
import client.message.Message;
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
                processClientRequest();
            }
        } catch (Exception e) {
            System.out.println("Client closed the connection");
        }
    }

    private void processClientRequest() throws IOException {
        Message message = null;
        try {
            message = (Message)ois.readObject();
            if (message.getType() == MES_TYPE.GET_LAN) {
                Message response = new Message(MES_TYPE.GET_LAN, lan);
                oos.writeObject(response);
                oos.flush();
                System.out.println("LAN sent to client");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
