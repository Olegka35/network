package service.lan;

import service.elements.IElement;
import service.graph.Graph;
import service.ip.IP;
import service.ip.Port;

import java.util.*;

public class MyLAN extends AbstractLAN {
    public MyLAN() {
        graph = new Graph<IElement>();
    }


    @Override
    public Boolean addElement(IElement element) {
        if(findElement(element.getName()) != null) {
            System.out.println("Element with this name is already exist");
            return false;
        }
        graph.addVertice(element);
        element.setLAN(this);
        return true;
    }

    @Override
    public Boolean removeElement(String name) {
        IElement element = findElement(name);
        if(element == null)
            return false;
        List<Port> ports = element.getPorts();
        for(Port port: ports) {
            if(port.getElement() != null)
                return false;
        }
        graph.removeVertex(element);
        return true;
    }

    @Override
    public IElement findElement(String name) {
        Set<IElement> elements = graph.getAllVertices();

        try {
            IP address = new IP(name);

            for (IElement element : elements) {
                if (element.getIPs().contains(address))
                    return element;
            }
        } catch (Exception e) {
            for (IElement element : elements) {
                if (element.getName().equals(name))
                    return element;
            }
        }

        return null;
    }

    @Override
    public IElement findElement(IP address) {
        Set<IElement> elements = graph.getAllVertices();
        for (IElement element : elements) {
            if (element.getIPs().contains(address))
                return element;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Element list:\n");
        Set<IElement> elements = graph.getAllVertices();
        for (IElement element : elements) {
            sb.append(element).append(":\n");
            List<IElement> connectedElements = graph.getAdjacentVertices(element);
            if (connectedElements != null) {
                for (IElement e : connectedElements) {
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

    @Override
    public Boolean connectTwoElements(IElement e1, IElement e2) {
        if (!e1.checkConnectAbility(e2) || !e2.checkConnectAbility(e1)) return false;
        Port port1 = e1.getPortForConnectWith(e2);
        Port port2 = e2.getPortForConnectWith(e1);
        port1.setElement(e2);
        port2.setElement(e1);
        graph.addEdge(e1, e2);
        graph.addEdge(e2, e1);
        return true;
    }

    @Override
    public Boolean disconnectElements(IElement e1, IElement e2) {
        if(e1.getPortByElement(e2) == null)
            return false;
        Port port1 = e1.getPortByElement(e2);
        Port port2 = e2.getPortByElement(e1);
        port1.setElement(null);
        port2.setElement(null);
        graph.removeEdge(e1, e2);
        graph.removeEdge(e2, e1);
        return true;
    }

    @Override
    public List<IElement> pingElements(IElement e1, IElement e2) {
        Queue<IElement> queue = new PriorityQueue<>();
        List<IElement> passedElements = new ArrayList<>();
        Map<IElement, IElement> prevElement = new HashMap<>();
        boolean result = false;

        queue.add(e1);
        prevElement.put(e1, null);
        while(!queue.isEmpty()) {
            IElement current = queue.remove();
            if(current == e2) {
                result = true;
                break;
            }
            passedElements.add(current);
            for(Port port: current.getPorts()) {
                IElement next = port.getElement();
                if(next != null) {
                    if(!passedElements.contains(next) && !queue.contains(next)) {
                        queue.add(next);
                        prevElement.put(next, current);
                    }
                }
            }
        }

        if(result) {
            List<IElement> way = new LinkedList<>();
            way.add(e2);
            IElement prev = prevElement.get(e2);
            while(prev != null) {
                way.add(prev);
                prev = prevElement.get(prev);
            }
            Collections.reverse(way);
            return way;
        }

        return null;
    }
}
