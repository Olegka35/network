package service;

import client.message.MES_TYPE;
import client.message.Message;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.router.MyRouter;
import service.elements.router.Router;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
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
                oos.writeObject(new Message(MES_TYPE.GET_LAN, lan));
                oos.flush();
                System.out.println("LAN sent to client");
            }
            else if(message.getType() == MES_TYPE.CREATE_ROUTER
                    || message.getType() == MES_TYPE.CREATE_NIC
                    || message.getType() == MES_TYPE.CREATE_SWITCH) {
                String[] params = (String[])message.getData();
                if(lan.findElement(params[1]) != null) {
                    oos.writeObject(new Message(MES_TYPE.ERROR, "Element with this name is already exist"));
                    oos.flush();
                    return;
                }
                if(message.getType() == MES_TYPE.CREATE_ROUTER) {
                    Router router = new MyRouter(Integer.parseInt(params[2]), params[1]);
                    lan.addElement(router);
                    oos.writeObject(new Message(MES_TYPE.CREATE_ROUTER, router));
                } else if(message.getType() == MES_TYPE.CREATE_NIC) {
                    NIC nic = new MyNIC(params[1]);
                    lan.addElement(nic);
                    oos.writeObject(new Message(MES_TYPE.CREATE_NIC, nic));
                } else if(message.getType() == MES_TYPE.CREATE_SWITCH) {
                    Switch aSwitch = new MySwitch(Integer.parseInt(params[2]), params[1]);
                    lan.addElement(aSwitch);
                    oos.writeObject(new Message(MES_TYPE.CREATE_SWITCH, aSwitch));
                }
                oos.flush();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
