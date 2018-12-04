package service.elements.router;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;

import java.net.InetAddress;

public class MyRouter extends Element implements Router {
    public MyRouter(Integer ports) {
        this.ports = ports;
    }

    public Integer getPortsNumber() {
        return ports;
    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public Boolean checkElement() {
        return null;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(element instanceof NIC) return false;
        if(element instanceof Router) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("MyRouter <ID: %d>", id);
    }

}
