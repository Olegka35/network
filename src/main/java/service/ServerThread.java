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
                    broadcast(new Message(MES_TYPE.CREATE_ROUTER, router));
                } else if(message.getType() == MES_TYPE.CREATE_NIC) {
                    NIC nic = new MyNIC(params[1]);
                    lan.addElement(nic);
                    broadcast(new Message(MES_TYPE.CREATE_NIC, nic));
                } else if(message.getType() == MES_TYPE.CREATE_SWITCH) {
                    Switch aSwitch = new MySwitch(Integer.parseInt(params[2]), params[1]);
                    lan.addElement(aSwitch);
                    broadcast(new Message(MES_TYPE.CREATE_SWITCH, aSwitch));
                }
            }
            else if(message.getType() == MES_TYPE.GET_ELEMENT_INFO) {
                String param = message.getData().toString();
                IElement element = lan.findElement(param);
                oos.writeObject(new Message(MES_TYPE.GET_ELEMENT_INFO, element));
                oos.flush();
            }
            else if(message.getType() == MES_TYPE.CONFIGURE_ELEMENT) {
                String[] params = (String[])message.getData();
                IElement element = lan.findElement(params[1]);
                IP newIP;
                Integer mask;
                if(element == null) {
                    oos.writeObject(new Message(MES_TYPE.ERROR, "Element not found"));
                    oos.flush();
                    return;
                }
                if(element instanceof Switch) {
                    oos.writeObject(new Message(MES_TYPE.ERROR, "Switch cannot be configured"));
                    oos.flush();
                    return;
                }
                if(element instanceof NIC) {
                    try {
                        newIP = new IP(params[2]);
                    } catch(Exception e) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Incorrect IP address"));
                        oos.flush();
                        return;
                    }
                    mask = Integer.parseInt(params[3]);
                    if(mask <= 0 || mask > 32) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Incorrect mask"));
                        oos.flush();
                        return;
                    }
                    Boolean result = ((NIC) element).configureNIC(newIP, mask);
                    if(!result) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Error while configuring NIC"));
                        oos.flush();
                        return;
                    }
                    broadcast(new Message(MES_TYPE.CONFIGURE_ELEMENT, String.format("NIC %s %s", element.getName(), ((NIC) element).getIP())));
                }
                else if(element instanceof Router) {
                    int port = Integer.parseInt(params[2]);
                    if(port < 0 || port >= element.getPorts().size()) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Incorrect port number"));
                        oos.flush();
                        return;
                    }
                    try {
                        newIP = new IP(params[3]);
                    } catch(Exception e) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Incorrect IP address"));
                        oos.flush();
                        return;
                    }
                    mask = Integer.parseInt(params[4]);
                    if(mask <= 0 || mask > 32) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Incorrect mask"));
                        oos.flush();
                        return;
                    }
                    Boolean result = ((Router) element).configurePort(port, newIP, mask);
                    if(!result) {
                        oos.writeObject(new Message(MES_TYPE.ERROR, "Error while configuring Router"));
                        oos.flush();
                        return;
                    }
                    broadcast(new Message(MES_TYPE.CONFIGURE_ELEMENT, String.format("Router %s", element.getName())));
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void broadcast(Message message) {
        try {
            for(ServerThread client: clients) {
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
