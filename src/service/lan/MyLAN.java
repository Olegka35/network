package service.lan;

import service.elements.IElement;
import service.graph.Graph;

import java.net.InetAddress;

public class MyLAN extends AbstractLAN {
    private static MyLAN myLan;
    private MyLAN() {
        elements = new Graph<IElement>();
    }

    public static LAN getLAN() {
        if(myLan == null)
            myLan = new MyLAN();
        return myLan;
    }

    @Override
    public Boolean checkLAN() {
        return null;
    }

    @Override
    public Boolean addElement(IElement element) {
        elements.addVertice(element);
        return true;
    }

    @Override
    public IElement findElement(InetAddress address) {
        return null;
    }

    @Override
    public String toString() {
        return "MyLAN{" +
                "elements=" + elements +
                '}';
    }

}
