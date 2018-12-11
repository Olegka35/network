package service.lan;

import service.elements.IElement;
import service.graph.Graph;
import service.ip.IP;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyLAN extends AbstractLAN {
    public MyLAN() {
        graph = new Graph<IElement>();
    }

    @Override
    public Boolean checkLAN() {
        return null;
    }

    @Override
    public Boolean addElement(IElement element) {
        graph.addVertice(element);
        element.setLAN(this);
        return true;
    }

    @Override
    public IElement findElement(IP address) {
        Set<IElement> elements = graph.getAllVertices();
        for(IElement element: elements) {
            if(element.getIPs().contains(address))
                return element;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Element list:\n");
        Set<IElement> elements = graph.getAllVertices();
        for(IElement element: elements) {
            sb.append(element).append(":\n");
            List<IElement> connectedElements = graph.getAdjacentVertices(element);
            if(connectedElements != null) {
                for(IElement e: connectedElements) {
                    sb.append("    ").append(e).append('\n');
                }
            }
        }
        return sb.toString();
    }

    @Override
    public Boolean checkConnectAbility(IElement e1, IElement e2) {
        return e1.checkConnectAbility(e2) & e2.checkConnectAbility(e1);
    }

    /*@Override
    public Boolean connectTwoElements(IElement e1, IElement e2) {
        if(!e1.checkConnectAbility(e2) || !e2.checkConnectAbility(e1)) return false;
        graph.addEdge(e1, e2);
        graph.addEdge(e2, e1);
        return null;
    }*/
}
