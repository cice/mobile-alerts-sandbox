package digital.theisen.messages;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.Inet4Address;

public class GetConfigMessage extends ConfigMessage {
    public static final short COMMAND = 3;
    public static final int DHCP_IP_POS = 11;
    public static final int USE_DHCP_POS = 15;
    public static final int FIXED_IP_POS = 16;
    public static final int FIXED_NET_MASK_POS = 20;
    public static final int FIXED_GATEWAY_POS = 24;
    public static final int DEVICE_NAME_POS = 28;
    public static final int DEVICE_NAME_LEN = 21;
    public static final int DATA_SERVER_NAME_POS = 49;
    public static final int DATA_SERVER_NAME_LEN = 65;
    public static final int USE_PROXY_POS = 114;
    public static final int PROXY_SERVER_NAME_POS = 115;
    public static final int PROXY_SERVER_NAME_LEN = 65;
    public static final int PROXY_SERVER_PORT_POS = 180;
    public static final int FIXED_DNS_POS = 182;
    public Inet4Address DHCPIp;

    public GetConfigMessage(byte[] gatewayId) {
        setGatewayId(gatewayId);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.GET_CONFIG;
    }

    public byte[] getBytes() {
        throw new NotImplementedException();
    }

    public SetConfigMessage setConfigMessage() {
        SetConfigMessage message = new SetConfigMessage();

        message.setGatewayId(getGatewayId());
        message.DataServerName = DataServerName;
        message.DeviceName = DeviceName;
        message.FixedDNSIp = FixedDNSIp;
        message.FixedIp = FixedIp;
        message.FixedGatewayIp = FixedGatewayIp;
        message.FixedNetMask = FixedNetMask;
        message.UseDHCP = UseDHCP;
        message.ProxyServerName = ProxyServerName;
        message.UseProxy = UseProxy;

        return message;
    }

}
