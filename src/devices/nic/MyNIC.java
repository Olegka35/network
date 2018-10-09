package devices.nic;

import devices.switches.Switch;

public class MyNIC implements NIC {
    private Integer address;
    private Switch _switch;

    public Integer getAddress() {
        return address;
    }

    public Switch getSwitch() {
        return _switch;
    }

    @Override
    public Integer connect(Switch _switch) {
        return null;
    }
}
