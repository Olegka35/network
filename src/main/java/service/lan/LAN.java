package service.lan;

import service.elements.IElement;
import service.graph.Graph;
import service.ip.IP;

import java.io.Serializable;
import java.net.InetAddress;

public interface LAN extends Serializable {
    Boolean addElement(IElement element);
    IElement findElement(IP address);
    String toString();
    Boolean checkConnectAbility(IElement e1, IElement e2);
    Boolean connectTwoElements(IElement e1, IElement e2);
    Graph getGraph();
}
