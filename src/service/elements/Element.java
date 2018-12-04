package service.elements;

import service.ip.IP;
import service.lan.LAN;

import java.net.InetAddress;
import java.util.List;

public abstract class Element implements IElement {
    protected LAN lan;
    protected Integer id;
    protected List<IP> ip;
    protected Integer ports;

    public LAN getLAN() {
        return lan;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    @Override
    public Boolean connectWith(IElement element) {
        return lan.connectTwoElements(this, element);
    }
}
