package service.lan;

import service.elements.IElement;
import service.graph.Graph;
import service.ip.IP;

import java.io.Serializable;
import java.net.InetAddress;

public interface LAN extends Serializable {
    Boolean checkLAN();
    Integer addElement(IElement element, IP ip);
    IElement findElement(InetAddress address);
    String toString();
    Boolean connectTwoElements(IElement e1, IElement e2);
}
