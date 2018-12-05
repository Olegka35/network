package service.ip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class IP implements Serializable {
    private ArrayList<Integer> ip;

    public IP(String address) throws Exception {
        ip = new ArrayList<>();
        String[] strArr = address.split("\\.");
        if(strArr.length != 4) throw new Exception("Incorrect IP address. Must be in format 111.111.222.222.");
        for(String str: strArr) {
            Integer part = Integer.parseInt(str);
            if(part < 0 || part > 255) throw new Exception("Incorrect IP address. Parts must be in interval 0 - 255.");
            ip.add(part);
        }
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d.%d", ip.get(0), ip.get(1), ip.get(2), ip.get(3));
    }

    public IP getNetIpByMask(Integer mask) throws Exception {
        if(mask < 1 || mask > 32) throw new Exception("Incorrect mask");
        Integer[] netIp = new Integer[4];
        Integer[] maskPart = { 0, 0, 0, 0 };
        for(int i = 0; mask > 0; ++i) {
            maskPart[i] = (mask >= 8) ? 8 : mask;
            mask -= 8;
        }

        for(int i = 0; i < 4; ++i) {
            netIp[i] = ip.get(i) & getBinValue(maskPart[i]);
        }

        String strIp = String.format("%d.%d.%d.%d", netIp[0], netIp[1], netIp[2], netIp[3]);
        return new IP(strIp);
    }

    private Integer getBinValue(Integer num) {
        switch (num) {
            case 0: return 0b0;
            case 1: return 0b10000000;
            case 2: return 0b11000000;
            case 3: return 0b11100000;
            case 4: return 0b11110000;
            case 5: return 0b11111000;
            case 6: return 0b11111100;
            case 7: return 0b11111110;
            case 8: return 0b11111111;
            default: return -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IP ip1 = (IP) o;
        return Objects.equals(ip, ip1.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
