package service.elements.switches;

import service.elements.Element;

import java.net.InetAddress;

public class MySwitch extends Element implements Switch {

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public Boolean checkElement() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("MySwitch <ID: %d>", id);
    }

    @Override
    public Integer getID() {
        return id;
    }
}
