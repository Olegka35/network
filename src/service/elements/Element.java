package service.elements;

import service.ip.IP;
import service.lan.LAN;

import java.net.InetAddress;
import java.util.List;

public abstract class Element implements IElement {
    protected LAN lan;
    protected List<IP> ip;
    protected Integer ports;

    @Override
    public LAN getLAN() {
        return lan;
    }

    @Override
    public void setLAN(LAN lan) {
        this.lan = lan;
    }

    @Override
    public List<IP> getIPs() {
        return ip;
    }
}
