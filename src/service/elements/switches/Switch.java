package service.elements.switches;

import service.elements.IElement;
import service.ip.IP;

public interface Switch extends IElement {
    Boolean configureSwitch(IP address, Integer mask);
    Integer getMask();
}
