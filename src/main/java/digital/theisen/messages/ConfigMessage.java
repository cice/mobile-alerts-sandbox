package digital.theisen.messages;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.net.Inet4Address;

@NonNullByDefault
public abstract class ConfigMessage implements IMessage {

    public boolean UseDHCP;
    public Inet4Address FixedIp;
    public Inet4Address FixedNetMask;
    public Inet4Address FixedGatewayIp;
    public String DeviceName;
    public String DataServerName;
    public boolean UseProxy;
    public String ProxyServerName;
    public short ProxyPort;
    public Inet4Address FixedDNSIp;
    private byte[] GatewayId;

    public byte[] getGatewayId() {
        return GatewayId;
    }

    public void setGatewayId(byte[] gatewayId) {
        GatewayId = gatewayId;
    }
}
