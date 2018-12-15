package service.elements.nic;

import service.elements.Element;
import service.elements.IElement;
import service.elements.router.Router;
import service.elements.switches.Switch;
import service.ip.IP;
import service.ip.Port;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MyNIC extends Element implements NIC {
    public MyNIC() {
        ports = new ArrayList<Port>();
        ports.add(new Port());
    }

    public Port getPort() {
        return getPorts().get(0);
    }

    @Override
    public IP getIP() {
        return getPorts().get(0).getAddress();
    }

    @Override
    public Integer getMask() {
        return getPorts().get(0).getMask();
    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public String toString() {
        return String.format("MyNIC <IP: %s>", getIP());
    }


    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(element instanceof NIC) return false;
        if(element instanceof Switch) return true;
        if(element instanceof Router) {
            try {
                IP ip = getIP();

                List<Port> routerPorts = ((Router) element).findPortsByIp(ip, getMask());
                if(routerPorts.isEmpty()) return false;
                for(Port port: routerPorts) {
                    if(port.getElement() == null)
                        return true;
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public Port getPortForConnectWith(IElement element) {
        return getPort();
    }

    @Override
    public Boolean configureNIC(IP address, Integer mask) {
        if(lan.findElement(address) != null) return false;
        Port port = getPort();
        port.setAddress(address);
        port.setMask(mask);
        return true;
    }
}
