package service.elements;

import service.ip.IP;
import service.ip.Port;
import service.lan.LAN;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;

public interface IElement extends Serializable {
    void sendMessage(InetAddress address, String message, String info);
    Boolean checkElement();
    String toString();
    Boolean checkConnectAbility(IElement element);
    //Boolean connectWith(IElement element);
    List<Port> getPorts();
    List<IP> getIPs();
    void setLAN(LAN lan);
    LAN getLAN();
    Port getPortByElement(IElement element);
}
