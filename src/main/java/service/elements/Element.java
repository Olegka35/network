package service.elements;

import service.ip.IP;
import service.ip.Port;
import service.lan.LAN;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public abstract class Element implements IElement {
    protected LAN lan;
    protected List<Port> ports;

    @Override
    public LAN getLAN() {
        return lan;
    }

    @Override
    public void setLAN(LAN lan) {
        this.lan = lan;
    }

    @Override
    public List<Port> getPorts() {
        return ports;
    }

    @Override
    public List<IP> getIPs() {
        List<Port> ports = getPorts();
        List<IP> ips = new ArrayList<>();
        for(Port port: ports) {
            if(port == null) continue;
            ips.add(port.getAddress());
        }
        return ips;
    }

    @Override
    public Port getPortByElement(IElement element) {
        for(Port port: ports) {
            if(port.getElement() == element)
                return port;
        }
        return null;
    }
}
