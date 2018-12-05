package service.elements.nic;

import service.elements.IElement;
import service.elements.switches.Switch;
import service.ip.IP;

import java.net.InetAddress;

public interface NIC extends IElement {
    Boolean configureNIC(IP address);
}
