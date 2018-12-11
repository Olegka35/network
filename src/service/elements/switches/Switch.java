package service.elements.switches;

import service.elements.IElement;
import service.ip.IP;

import java.util.List;

public interface Switch extends IElement {
    List<IElement> getConnectedElements();
}
