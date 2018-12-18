package client.draw;

import service.elements.IElement;
import service.elements.nic.NIC;
import service.elements.switches.Switch;
import service.graph.Graph;
import service.lan.LAN;

import java.util.List;
import java.util.Set;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class GraphDraw {
    private LAN lan;
    private org.graphstream.graph.Graph result;

    public GraphDraw(LAN lan) {
        this.lan = lan;
        result = new SingleGraph("Network");
    }

    public void draw() {
        Graph graph = lan.getGraph();

        Set<IElement> elements = graph.getAllVertices();
        for (IElement element : elements) {
            addElement(element);
        }
        for (IElement element : elements) {
            String id = getElementID(element);

            List<IElement> connectedElements = graph.getAdjacentVertices(element);
            if (connectedElements != null) {
                for (IElement e : connectedElements) {
                    String thisID = getElementID(e);
                    if(!result.getNode(id).hasEdgeBetween(thisID))
                        result.addEdge(id+thisID, id, thisID);
                }
            }
        }
        result.display();
    }

    private String getElementID(IElement element) {
        if(element instanceof NIC)
            try {
                return ((NIC) element).getIP().toString();
            } catch (NullPointerException e) {
                return element.getName();
            }
        else
            return element.getName();
    }

    public void addElement(IElement element) {
        String id = getElementID(element), style, label = null;

        if(element instanceof NIC) {
            style = "size: 16px; fill-color: green;";
            label = id;
        }
        else if(element instanceof Switch) {
            style = "size: 16px; fill-color: blue;";
            label = element.getName();
        }
        else {
            style = "size: 16px; fill-color: black;";
            label = element.getName();
        }

        result.addNode(id);
        result.getNode(id).addAttribute("ui.style", style);
        if(label != null) result.getNode(id).addAttribute("ui.label", label);
    }

    public void removeElement(IElement element) {
        String id = getElementID(element);
        result.removeNode(id);
    }

    public void updateLabel(NIC element) {
        String id = getElementID(element);
        try {
            result.getNode(id).setAttribute("ui.label", element.getIP());
        } catch(Exception e) {
            result.getNode(element.getName()).setAttribute("ui.label", element.getIP());
        }
    }

    public void addEdge(IElement e1, IElement e2) {
        String id1 = getElementID(e1);
        String id2 = getElementID(e2);
        if(result.getNode(id1) == null) id1 = e1.getName();
        if(result.getNode(id2) == null) id2 = e2.getName();
        result.addEdge(id1+id2, id1, id2);
    }

    public void removeEdge(IElement e1, IElement e2) {
        String id1 = getElementID(e1);
        String id2 = getElementID(e2);
        if(result.getNode(id1) == null) id1 = e1.getName();
        if(result.getNode(id2) == null) id2 = e2.getName();
        result.removeEdge(id1, id2);
    }
}
