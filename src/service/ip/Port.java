package service.ip;

import service.elements.IElement;

public class Port {
    private IP address;
    private Integer mask;
    private IElement element;

    public IP getAddress() {
        return address;
    }

    public void setAddress(IP address) {
        this.address = address;
    }

    public IElement getElement() {
        return element;
    }

    public void setElement(IElement element) {
        this.element = element;
    }

    public Integer getMask() {
        return mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }

    @Override
    public String toString() {
        return "Port{" +
                "IP=" + address +
                ", mask=" + mask +
                ", element=" + element +
                '}';
    }
}
