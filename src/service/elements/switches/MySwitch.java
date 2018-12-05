package service.elements.switches;

import service.elements.Element;
import service.elements.IElement;
import service.ip.IP;

import java.net.InetAddress;
import java.util.ArrayList;

public class MySwitch extends Element implements Switch {
    Integer mask;

    public MySwitch() {
        ip = new ArrayList<IP>();
        ip.add(null);
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
        return String.format("MySwitch <ID: %s>", getIP());
    }

    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(element instanceof Switch) return false;
        return true;
    }

    @Override
    public Boolean configureSwitch(IP address, Integer mask) {
        if(lan.findElement(address) != null) return false;
        this.mask = mask;
        ip.set(0, address);
        ports = (int)Math.pow(mask, 2)-1;
        return true;
    }

    public Integer getMask() {
        return mask;
    }
}
