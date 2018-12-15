package client;

import service.elements.IElement;
import service.elements.nic.NIC;
import service.lan.LAN;
import service.lan.MyLAN;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ClientInstance("localhost", 4004);
    }
}
