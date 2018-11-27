package service.elements;

import service.lan.LAN;

import java.net.InetAddress;

public abstract class Element implements IElement {
    protected LAN lan;
    protected InetAddress address;
    protected Integer id;

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
