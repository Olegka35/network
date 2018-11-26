package service.elements;

import service.graph.Graph;

import java.net.InetAddress;
import java.util.List;

public abstract class Element implements IElement {
    protected Graph<IElement> graph;
    protected InetAddress address;
    protected Integer id;

    public Graph<IElement> getGraph() {
        return graph;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getID() {
        return id;
    }
}
