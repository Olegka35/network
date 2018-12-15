package client;

import client.draw.GraphDraw;
import client.message.MES_TYPE;
import client.message.Message;
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
                    Message message = (Message)ois.readObject();
                    if(message.getType() == MES_TYPE.GET_LAN) {
                        LAN lan = (LAN) message.getData();
                        GraphDraw graph = new GraphDraw(lan);
                        graph.draw();
                    }
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
                    String command = reader.readLine();
                    Message request = new Message();

                    if(command.equals("getLAN")) {
                        request.setType(MES_TYPE.GET_LAN);
                        request.setData(command);
                    }

                    oos.writeObject(request);
                    oos.flush();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
