package service.elements;

import java.net.InetAddress;

public interface IElement {
    void deleteElement();
    void sendMessage(InetAddress address, String message, String info);
    Boolean checkElement();
}
