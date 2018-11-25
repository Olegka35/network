package service.lan;

import service.elements.IElement;

import java.net.InetAddress;

public interface LAN {
    Boolean checkLAN();
    Boolean addElement(IElement element);
    IElement findElement(InetAddress address);
    String toString();
}
