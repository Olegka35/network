package service.elements.nic;

import service.elements.Element;

import java.net.InetAddress;

public class MyNIC extends Element implements NIC {
    @Override
    public void deleteElement() {

    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public Boolean checkElement() {
        return null;
    }

    @Override
    public String toString() {
        return "NIC (" + this.address + ")";
    }
}
