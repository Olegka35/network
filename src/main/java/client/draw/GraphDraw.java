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

}
