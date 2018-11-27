package service.lan;

import service.elements.IElement;
import service.graph.Graph;

import java.io.Serializable;
import java.net.InetAddress;

public interface LAN extends Serializable {
    Boolean checkLAN();
    Integer addElement(IElement element);
    IElement findElement(InetAddress address);
    String toString();
}
