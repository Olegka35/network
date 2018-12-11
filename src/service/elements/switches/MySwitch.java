package service.elements.switches;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;
import service.ip.IP;
import service.ip.Port;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MySwitch extends Element implements Switch {
    private String name;

    public MySwitch(Integer portsNumber, String name) {
        ports = new ArrayList<Port>();
        for(int i = 0; i < portsNumber; ++i) {
            ports.add(new Port());
        }
        this.name = name;
    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public String toString() {
        return String.format("MySwitch <Name: %s>", name);
    }

    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(getFreePort() == null) return false;
        if(element instanceof Switch) return false;
        if(element instanceof NIC) {
            try {
                IP ip = ((NIC) element).getIP();
                Integer mask = ((NIC) element).getMask();
                for (Port port : getPorts()) {
                    if(port.getElement() == null) continue;
                    Port port1 = port.getElement().getPortByElement(this);
                    IP ip1 = port1.getAddress();
                    Integer mask1 = port1.getMask();
                    if (!ip1.getNetIpByMask(mask1).equals(ip.getNetIpByMask(mask)))
                        return false;
                }
                return true;
            }
            catch (Exception e ) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Port getPortForConnectWith(IElement element) {
        return getFreePort();
    }


    public String getName() {
        return name;
    }

    private Port getFreePort() {
        for(Port port: ports) {
            if(port.getElement() == null)
                return port;
        }
        return null;
    }

    @Override
    public List<IElement> getConnectedElements() {
        List<IElement> elements = new ArrayList<>();

        for(Port port: getPorts()) {
            if(port.getElement() != null)
                elements.add(port.getElement());
        }
        return elements;
    }
}
