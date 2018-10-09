package devices.switches;

public interface Switch {
    void register(Integer ip, Integer mask);
    void sendMessage(Integer ip, String message);
}
