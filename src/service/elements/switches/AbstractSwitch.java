package service.elements.switches;

import service.elements.Element;
import service.elements.IElement;
import service.elements.nic.NIC;

import java.net.InetAddress;
import java.util.List;

public abstract class AbstractSwitch extends Element implements Switch {
    private Integer mask;
}
