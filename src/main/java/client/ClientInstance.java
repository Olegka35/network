package client;

import client.draw.GraphDraw;
import client.message.MES_TYPE;
import client.message.Message;
import service.elements.IElement;
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
        private GraphDraw graph;
        @Override
        public void run() {
            try {
                String type;
                while (true) {
                    Message message = (Message)ois.readObject();
                    MES_TYPE mes_type = message.getType();
                    if(mes_type == MES_TYPE.GET_LAN) {
                        LAN lan = (LAN) message.getData();
                        graph = new GraphDraw(lan);
                        graph.draw();
                    }
                    else if(mes_type == MES_TYPE.CREATE_NIC || mes_type == MES_TYPE.CREATE_SWITCH || mes_type == MES_TYPE.CREATE_ROUTER) {
                        graph.addElement((IElement)message.getData());
                        System.out.println("Element " + message.getData().toString() + " was added to the network");
                    }
                    else if(mes_type == MES_TYPE.ERROR) {
                        System.out.println("ERROR: " + message.getData().toString());
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
                Message request = new Message();
                request.setType(MES_TYPE.GET_LAN);
                oos.writeObject(request);
                oos.flush();

                while(true) {
                    System.out.println("Enter command:");
                    request = new Message();

                    String command = reader.readLine();
                    String commands[] = command.split(" ");
                    if(commands[0].equals("create_router")) {
                        if(commands.length < 3 || commands[1].isEmpty() || commands[2].isEmpty() || Integer.parseInt(commands[2]) <= 0) {
                            System.out.println("User: create_router [router name] [ports number]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_ROUTER);
                        request.setData(commands);
                    } else if(commands[0].equals("create_switch")) {
                        if(commands.length < 3 || commands[1].isEmpty() || commands[2].isEmpty() || Integer.parseInt(commands[2]) <= 0) {
                            System.out.println("User: create_switch [switch name] [ports number]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_SWITCH);
                        request.setData(commands);
                    } else if(commands[0].equals("create_nic")) {
                        if(commands.length < 2 || commands[1].isEmpty()) {
                            System.out.println("User: create_nic [nic name]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_NIC);
                        request.setData(commands);
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
