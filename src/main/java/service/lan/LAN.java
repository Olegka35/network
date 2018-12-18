package service.lan;

import service.elements.IElement;
import service.graph.Graph;
import service.ip.IP;

import java.io.Serializable;
import java.util.List;

public interface LAN extends Serializable {
    Boolean addElement(IElement element);
    Boolean removeElement(String name);
    IElement findElement(String name);
    IElement findElement(IP address);
    String toString();
    Boolean checkConnectAbility(IElement e1, IElement e2);
    Boolean connectTwoElements(IElement e1, IElement e2);
    Boolean disconnectElements(IElement e1, IElement e2);
    Graph getGraph();
    List<IElement> pingElements(IElement e1, IElement e2);
}
