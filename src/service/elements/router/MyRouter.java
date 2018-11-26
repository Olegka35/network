package service.elements.router;

import service.elements.Element;

import java.net.InetAddress;

public class MyRouter extends Element implements IRouter {
    @Override
    public void deleteElement() {

    }

    @Override
    public void sendMessage(InetAddress address, String message, String info) {

    }

    @Override
    public Boolean checkElement() {
        return null;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("MyRouter <ID: %d>", id);
    }

}
