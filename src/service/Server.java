package service;

import service.elements.IElement;
import service.elements.nic.MyNIC;
import service.elements.nic.NIC;
import service.elements.switches.MySwitch;
import service.elements.switches.Switch;
import service.graph.Graph;
import service.lan.LAN;
import service.lan.MyLAN;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = Inet4Address.getByName("192.168.0.100");
        System.out.println(address.toString());

        LAN lan = MyLAN.getLAN();
        NIC nic1 = new MyNIC();
        Switch switch1 = new MySwitch();

        lan.addElement(nic1);
        lan.addElement(switch1);
        //test.addEdge("2", "1");
        // test.removeVertex("1");
        System.out.println(lan);
    }
}
