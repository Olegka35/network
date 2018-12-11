package service.elements.router;

import service.elements.IElement;
import service.ip.IP;
import service.ip.Port;

import java.util.List;

public interface Router extends IElement {
    Boolean configurePort(Integer port, IP address, Integer mask);
    List<Port> getPorts();
    List<Port> findPortsByIp(IP ip, Integer mask);
    IP getPortAddress(Integer port);
}
