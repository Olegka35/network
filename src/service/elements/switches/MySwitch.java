package service.elements.switches;

import java.net.InetAddress;

public class MySwitch extends AbstractSwitch {
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
        return String.format("MySwitch <ID: %d>", id);
    }

    @Override
    public Integer getID() {
        return id;
    }
}
