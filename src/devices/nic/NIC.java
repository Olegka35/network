package devices.nic;

import devices.switches.Switch;

public interface NIC {
    Integer connect(Switch _switch);
    Boolean sendMessage(Integer ip, String message);
    void readMessage(String message);
}
