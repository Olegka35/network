package service.elements;

import java.io.Serializable;
import java.net.InetAddress;

public interface IElement extends Serializable {
    void sendMessage(InetAddress address, String message, String info);
    Boolean checkElement();
    String toString();
    Integer getID();
    void setID(Integer id);
    Boolean checkConnectAbility(IElement element);
    Boolean connectWith(IElement element);
}
