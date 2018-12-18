package service;

import client.message.MES_TYPE;
import client.message.Message;
import service.elements.IElement;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.router.MyRouter;
import service.elements.router.Router;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
import service.ip.IP;
import service.lan.LAN;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;

import static client.message.MES_TYPE.GET_LAN;

public class ServerThread extends Thread {
    private Socket socket;
    private LAN lan;
    private LinkedList<ServerThread> clients;

    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ServerThread(Socket socket, LAN lan, LinkedList<ServerThread> clients) throws IOException {
        this.socket = socket;
        this.lan = lan;
        this.clients = clients;
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
            clients.remove(this);
            e.printStackTrace();
            System.out.println("Client closed the connection");
        }
    }

    private void processClientRequest() throws IOException {
        Message message = null;
        try {
            message = (Message)ois.readObject();

            CommandManager manager = new CommandManager(message);
            Map<String, Object> response = manager.produce(lan);
            Message responseMessage = (Message)response.get("message");

            if(response.get("broadcast") != null) {
                broadcast(responseMessage);
            }
            else {
                oos.reset();
                oos.writeObject(responseMessage);
                oos.flush();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(Message message) {
        try {
            for(ServerThread client: clients) {
                client.getOos().reset();
                client.getOos().writeObject(message);
                client.getOos().flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }
}
