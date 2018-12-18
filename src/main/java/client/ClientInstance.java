package client;

import client.draw.GraphDraw;
import client.message.MES_TYPE;
import client.message.Message;
import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;
import service.lan.LAN;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientInstance {
    private Socket clientSocket;
    private BufferedReader reader;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

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
            while (true) {
                try {
                    Message message = (Message) ois.readObject();
                    MES_TYPE mes_type = message.getType();
                    if (mes_type == MES_TYPE.GET_LAN) {
                        LAN lan = (LAN) message.getData();
                        graph = new GraphDraw(lan);
                        graph.draw();
                    } else if (mes_type == MES_TYPE.CREATE_NIC || mes_type == MES_TYPE.CREATE_SWITCH || mes_type == MES_TYPE.CREATE_ROUTER) {
                        graph.addElement((IElement) message.getData());
                        System.out.println("Element " + message.getData().toString() + " was added to the network");
                        //graph.getLan().addElement((IElement)message.getData());
                    } else if (mes_type == MES_TYPE.GET_ELEMENT_INFO) {
                        IElement element = (IElement) message.getData();
                        System.out.println(element.toString());
                    } else if (mes_type == MES_TYPE.ERROR) {
                        System.out.println("ERROR: " + message.getData().toString());
                    } else if (mes_type == MES_TYPE.CONFIGURE_ELEMENT) {
                        IElement element = (IElement) message.getData();
                        System.out.println("Configure success: " + element);
                        if (element instanceof NIC) {
                            graph.updateLabel((NIC) element);
                        }
                    } else if (mes_type == MES_TYPE.REMOVE_ELEMENT) {
                        IElement element = (IElement) message.getData();
                        System.out.println("Element " + element + " was deleted");
                        graph.removeElement(element);
                    } else if (mes_type == MES_TYPE.CONNECT_ELEMENTS) {
                        List<IElement> elements = (List<IElement>) message.getData();
                        IElement e1 = elements.get(0);
                        IElement e2 = elements.get(1);
                        System.out.println("Elements " + e1 + " and " + e2 + " were connected");
                        graph.addEdge(e1, e2);
                    } else if (mes_type == MES_TYPE.DISCONNECT_ELEMENTS) {
                        List<IElement> elements = (List<IElement>) message.getData();
                        IElement e1 = elements.get(0);
                        IElement e2 = elements.get(1);
                        System.out.println("Elements " + e1 + " and " + e2 + " were disconnected");
                        graph.removeEdge(e1, e2);
                    } else if(mes_type == MES_TYPE.PING) {
                        List<IElement> way = (List<IElement>) message.getData();
                        if(way == null) System.out.println("Unable to get the path");
                        else {
                            System.out.println("Path:");
                            for(IElement element: way) {
                                System.out.println("- " + element);
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
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
                            System.out.println("Use: create_router [router name] [ports number]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_ROUTER);
                        request.setData(commands);
                    } else if(commands[0].equals("create_switch")) {
                        if(commands.length < 3 || commands[1].isEmpty() || commands[2].isEmpty() || Integer.parseInt(commands[2]) <= 0) {
                            System.out.println("Use: create_switch [switch name] [ports number]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_SWITCH);
                        request.setData(commands);
                    } else if(commands[0].equals("create_nic")) {
                        if(commands.length < 2 || commands[1].isEmpty()) {
                            System.out.println("Use: create_nic [nic name]");
                            continue;
                        }
                        request.setType(MES_TYPE.CREATE_NIC);
                        request.setData(commands);
                    } else if(commands[0].equals("get_info")) {
                        if(commands.length < 2) {
                            System.out.println("Use: get_info [IP / name]");
                            continue;
                        }
                        request.setType(MES_TYPE.GET_ELEMENT_INFO);
                        request.setData(commands[1]);
                    } else if(commands[0].equals("configure")) {
                        if(commands.length < 4) {
                            System.out.println("Use: configure [element IP/name] <port (FOR ROUTER)> [new IP] [new mask]");
                            continue;
                        }
                        request.setType(MES_TYPE.CONFIGURE_ELEMENT);
                        request.setData(commands);
                    } else if(commands[0].equals("remove")) {
                        if(commands.length < 2) {
                            System.out.println("Use: remove [element IP/name]");
                            continue;
                        }
                        request.setType(MES_TYPE.REMOVE_ELEMENT);
                        request.setData(commands[1]);
                    } else if(commands[0].equals("connect")) {
                        if(commands.length < 3) {
                            System.out.println("Use: connect [element1 IP/name][element2 IP/name]");
                            continue;
                        }
                        request.setType(MES_TYPE.CONNECT_ELEMENTS);
                        request.setData(commands);
                    } else if(commands[0].equals("disconnect")) {
                        if(commands.length < 3) {
                            System.out.println("Use: disconnect [element1 IP/name][element2 IP/name]");
                            continue;
                        }
                        request.setType(MES_TYPE.DISCONNECT_ELEMENTS);
                        request.setData(commands);
                    } else if(commands[0].equals("ping")) {
                        if(commands.length < 3) {
                            System.out.println("Use: ping [element1 IP/name][element2 IP/name]");
                            continue;
                        }
                        request.setType(MES_TYPE.PING);
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
