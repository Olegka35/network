package service.lan;

import service.elements.IElement;
import service.graph.Graph;

import java.net.InetAddress;
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
    public Integer addElement(IElement element) {
        Integer id = findFreeID();
        element.setID(id);
        graph.addVertice(element);
        return id;
    }

    @Override
    public IElement findElement(InetAddress address) {
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
    public Boolean connectTwoElements(IElement e1, IElement e2) {
        if(!e1.checkConnectAbility(e2) || !e2.checkConnectAbility(e1)) return false;
        graph.addEdge(e1, e2);
        graph.addEdge(e2, e1);
        return null;
    }

    private Integer findFreeID() {
        Set<Integer> ids = new HashSet<>();
        for(IElement element: graph.getAllVertices()) {
            ids.add(element.getID());
        }
        for(Integer i = 1; ; i++) {
            if(!ids.contains(i)) {
                return i;
            }
        }
    }

}
