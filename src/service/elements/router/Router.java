package service.elements.router;

import service.elements.IElement;
import service.ip.IP;

public interface Router extends IElement {
    Boolean configurePort(Integer port, IP address);
    IP getPortAddress(Integer port);
}
