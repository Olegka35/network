package service.elements.router;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;
import service.ip.IP;

import java.net.InetAddress;
import java.util.ArrayList;

public class MyRouter extends Element implements Router {
    public MyRouter(Integer ports) {
        this.ports = ports;
        ip = new ArrayList<>();
        for(int i = 0; i < ports; i++)
            ip.add(null);
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
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(element instanceof NIC) return false;
        if(element instanceof Router) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("MyRouter <ID: %s>", getIPs());
    }

    @Override
    public Boolean configurePort(Integer port, IP address) {
        if(lan.findElement(address) != null) return false;
        this.ip.set(port, address);
        return true;
    }
}
