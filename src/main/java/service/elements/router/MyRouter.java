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
    public MyRouter(Integer portsNumber, String name) {
        if(portsNumber <= 0) {
            System.out.println("Ports number must be more than 0");
            return;
        }
        ports = new ArrayList<Port>();
        for(int i = 0; i < portsNumber; ++i) {
            ports.add(new Port());
        }
        this.name = name;
    }

    public Integer getPortsNumber() {
        return ports.size();
    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public Boolean checkConnectAbility(IElement element) {
        if(element == null) return false;
        if(getFreePort() == null) return false;
        if(element instanceof Router) return false;


        for(Port port: ports) {
            try {
                if (port.getElement() != null) continue;

                IP ip = port.getAddress();
                Integer mask = port.getMask();

                if (element instanceof Switch) {
                    List<IElement> connectedToSwitch = ((Switch) element).getConnectedElements();
                    if (connectedToSwitch.isEmpty()) return true;

                    IElement randomElementOfSwitch = connectedToSwitch.get(0);
                    if (randomElementOfSwitch instanceof NIC) {
                        if (((NIC) randomElementOfSwitch).getIP().getNetIpByMask(((NIC) randomElementOfSwitch).getMask()).equals(ip.getNetIpByMask(mask)))
                            return true;
                    } else if (randomElementOfSwitch instanceof Router) {
                        List<Port> ports = ((Router) randomElementOfSwitch).findPortsByIp(ip, mask);
                        if (!ports.isEmpty()) return true;
                    }
                }

                if (element instanceof NIC) {
                    if (((NIC) element).getIP().getNetIpByMask(((NIC) element).getMask()).equals(ip.getNetIpByMask(mask)))
                        return true;
                }
            } catch(Exception e){
                return false;
            }
        }
        return false;
    }

    @Override
    public Port getPortForConnectWith(IElement element) {
        for(Port port: ports) {
            try {
                if (port.getElement() != null) continue;

                IP ip = port.getAddress();
                Integer mask = port.getMask();

                if (element instanceof NIC) {
                    NIC nic = (NIC) element;
                    if (nic.getIP().getNetIpByMask(nic.getMask()).equals(ip.getNetIpByMask(mask)))
                        return port;
                }
                if (element instanceof Switch) {
                    Switch _switch = (Switch)element;
                    List<IElement> switchElements = _switch.getConnectedElements();
                    if(switchElements.isEmpty()) return port;

                    IElement switchElement = switchElements.get(0);
                    if (switchElement instanceof NIC) {
                        if (((NIC) switchElement).getIP().getNetIpByMask(((NIC) switchElement).getMask()).equals(ip.getNetIpByMask(mask)))
                            return port;
                    } else if (switchElement instanceof Router) {
                        List<Port> ports = ((Router) switchElement).findPortsByIp(ip, mask);
                        if (!ports.isEmpty()) return port;
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("MyRouter <Name: %s, PORTS: %s>", name, getPorts());
    }

    @Override
    public Boolean configurePort(Integer port, IP address, Integer mask) {
        if(lan.findElement(address) != null) return false;
        if(this.ports.get(port).getElement() != null) return false;
        if(port >= 5) return false;
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
                if(port.getAddress().getNetIpByMask(port.getMask()).equals(ip.getNetIpByMask(mask)))
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
