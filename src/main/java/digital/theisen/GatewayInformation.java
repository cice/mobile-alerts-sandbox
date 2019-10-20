package digital.theisen;

import java.net.Inet4Address;

public class GatewayInformation {

    private final Inet4Address dhcpIp;
    private final byte[] gatewayId;

    public GatewayInformation(Inet4Address dhcpIp, byte[] gatewayId) {

        this.dhcpIp = dhcpIp;
        this.gatewayId = gatewayId;
    }
}
