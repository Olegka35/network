package devices.switches;

import devices.nic.NIC;

import java.util.List;

public class MySwitch implements Switch {
    private Integer ip;
    private Integer mask;
    List<NIC> nicList;

    public MySwitch(Integer ip, Integer mask) {
        register(ip, mask);
    }

    @Override
    public void register(Integer ip, Integer mask) {
        this.ip = ip;
        this.mask = mask;
    }
}
