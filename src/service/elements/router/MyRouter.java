package service.elements.router;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;
import service.elements.switches.Switch;
import service.ip.IP;
import service.ip.Port;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MyRouter extends Element implements Router {
    public MyRouter(Integer portsNumber) {
        ports = new ArrayList<Port>();
        for(int i = 0; i < portsNumber; ++i) {
            ports.add(new Port());
        }
    }

    public Integer getPortsNumber() {
        return ports.size();
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
        if(getFreePort() == null) return false;
        if(element instanceof Router) return false;

        if(element instanceof Switch) {
            for(Port port: ports) {
                if(port.getElement() == null) {
                    IP ip = port.getAddress();
                    Integer mask = port.getMask();

                    List<IElement> connectedToSwitch = ((Switch) element).getConnectedElements();
                    if(connectedToSwitch.isEmpty()) return true;

                    IElement randomElementOfSwitch = element.getPorts().get(0).getElement();
                    if(randomElementOfSwitch instanceof NIC) {
                        try {
                            if (((NIC) randomElementOfSwitch).getIP().getNetIpByMask(((NIC) randomElementOfSwitch).getMask()) == ip.getNetIpByMask(mask))
                                return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(randomElementOfSwitch instanceof Router) {
                        List<Port> ports = ((Router) randomElementOfSwitch).findPortsByIp(ip, mask);
                        if(!ports.isEmpty()) return true;
                    }

                }
            }
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("MyRouter <PORTS: %s>", getPorts());
    }

    @Override
    public Boolean configurePort(Integer port, IP address, Integer mask) {
        if(lan.findElement(address) != null) return false;
        this.ports.get(port).setAddress(address);
        this.ports.get(port).setMask(mask);
        return true;
    }

    @Override
    public List<Port> findPortsByIp(IP ip, Integer mask) {
        List<Port> ports = new ArrayList<Port>();
        for(Port port: getPorts()) {
            if(port == null || port.getAddress() == null) continue;
            try {
                if(port.getAddress().getNetIpByMask(mask) == ip.getNetIpByMask(mask))
                    ports.add(port);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ports;
    }

    @Override
    public IP getPortAddress(Integer port) {
        return getPorts().get(port).getAddress();
    }

    private Port getFreePort() {
        for(Port port: ports) {
            if(port.getElement() == null)
                return port;
        }
        return null;
    }
}
