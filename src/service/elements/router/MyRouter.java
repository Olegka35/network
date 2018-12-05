package service.elements.router;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;
import service.elements.switches.Switch;
import service.ip.IP;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MyRouter extends Element implements Router {
    IElement[] elements;
    public MyRouter(Integer ports) {
        this.ports = ports;
        ip = new ArrayList<>();
        for(int i = 0; i < ports; i++)
            ip.add(null);
        elements = new IElement[ports];
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

        if(element instanceof Switch) {
            List<IP> routerAddress = getIPs();
            IP switchAddress = element.getIPs().get(0);
            Integer mask = ((Switch)element).getMask();
            for(int i = 0; i < ports; ++i) {
                try {
                    if(elements[i] == null && ip.get(i) != null && ip.get(i).getNetIpByMask(mask).equals(switchAddress.getNetIpByMask(mask))) {
                        return true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
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

    @Override
    public IP getPortAddress(Integer port) {
        return ip.get(port);
    }
}
