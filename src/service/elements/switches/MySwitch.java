package service.elements.switches;

import service.elements.Element;
import service.elements.IElement;
import service.ip.IP;

import java.net.InetAddress;
import java.util.ArrayList;

public class MySwitch extends Element implements Switch {
    public MySwitch() {
        /*this.ip = new ArrayList<IP>();
        this.ip.add(ip);
        ports = (int)Math.pow(mask, 2)-1;*/
    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    public IP getIP() {
        return ip.get(0);
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

    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(element instanceof Switch) return false;
        return true;
    }
}
