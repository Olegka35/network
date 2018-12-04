package service.elements.nic;

import service.elements.Element;
import service.elements.IElement;
import service.elements.router.Router;
import service.ip.IP;

import java.net.InetAddress;
import java.util.ArrayList;

public class MyNIC extends Element implements NIC {
    public MyNIC() {
        ports = 1;
    }

    public IP getIP() {
        return ip.get(0);
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
        return String.format("MyNIC <ID: %d>", id);
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
}
