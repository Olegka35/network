package service.lan;

import service.elements.IElement;
import service.graph.Graph;

import java.util.List;

public abstract class AbstractLAN implements LAN {
    Graph<IElement> graph;

    @Override
    public Graph getGraph() {
        return graph;
    }
}
