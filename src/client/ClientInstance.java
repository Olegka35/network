package client;

import service.lan.LAN;

import java.io.*;
import java.net.Socket;

public class ClientInstance {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    public ClientInstance(String host, Integer port) throws IOException {
        clientSocket = new Socket("localhost", 4004);
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        ois = new ObjectInputStream(clientSocket.getInputStream());
        reader = new BufferedReader(new InputStreamReader(System.in));

        new ClientWriteThread().start();
        new ClientReadThread().start();
    }

    private class ClientReadThread extends Thread {
        @Override
        public void run() {
            try {
                String type;
                while (true) {
                    LAN lan = (LAN) ois.readObject();
                    System.out.println("LAN: " + lan);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientWriteThread extends Thread {
        @Override
        public void run() {
            try {
                while(true) {
                    System.out.println("Enter command:");
                    String message = reader.readLine();
                    oos.writeUTF(message);
                    oos.flush();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
