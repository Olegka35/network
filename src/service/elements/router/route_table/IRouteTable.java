package service.elements.router.route_table;

import service.elements.IElement;

import java.net.InetAddress;

public interface IRouteTable {
    IElement findNextElement(InetAddress address);
}
