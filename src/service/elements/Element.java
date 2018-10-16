package service.elements;

import java.net.InetAddress;
import java.util.List;

public abstract class Element implements IElement {
    protected List<IElement> connectedElements;
    protected InetAddress address;
}
