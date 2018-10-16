package service;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = Inet4Address.getByName("192.168.0.100");
        System.out.println(address.toString());

    }
}
