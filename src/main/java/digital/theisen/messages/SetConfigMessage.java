package digital.theisen.messages;

import java.nio.ByteBuffer;

public class SetConfigMessage extends ConfigMessage {
    public static final int USE_DHCP_POS = 10;
    public static final int FIXED_IP_POS = 11;
    public static final int FIXED_NET_MARK_POS = 15;
    public static final int FIXED_GATEWAY_POS = 19;
    public static final int DEVICE_NAME_POS = 23;
    public static final int DEVICE_NAME_LEN = 21;
    public static final int DATA_SERVER_NAME_POS = 44;
    public static final int DATA_SERVER_NAME_LEN = 65;
    public static final int USE_PROXY_POS = 109;
    public static final int PROXY_SERVER_NAME_POS = 110;
    public static final int PROXY_SERVER_NAME_LEN = 65;
    public static final int PROXY_SERVER_PORT_POS = 175;
    public static final int FIXED_DNS_POS = 177;
    public static short SIZE = 181;

    @Override
    public CommandType getCommandType() {
        return CommandType.SET_CONFIG;
    }

    public byte[] getBytes() {
        MessageHeader header = new MessageHeader(getCommandType().getValue(), getGatewayId(), SIZE);
        ByteBuffer body = ByteBuffer.allocate(SIZE);
        body.put(header.getBytes());

        body.put(USE_DHCP_POS, (byte) (UseDHCP ? 1 : 0));
        if (FixedIp != null) ByteBufferUtil.putBytes(body, FIXED_IP_POS, FixedIp.getAddress(), 4);
        if (FixedNetMask != null) ByteBufferUtil.putBytes(body, FIXED_NET_MARK_POS, FixedNetMask.getAddress(), 4);
        if (FixedGatewayIp != null) ByteBufferUtil.putBytes(body, FIXED_GATEWAY_POS, FixedGatewayIp.getAddress(), 4);
        if (FixedDNSIp != null) ByteBufferUtil.putBytes(body, FIXED_DNS_POS, FixedDNSIp.getAddress(), 4);
        if (DeviceName != null) ByteBufferUtil.putBytes(body, DEVICE_NAME_POS, DeviceName.getBytes(), DEVICE_NAME_LEN);
        if (DataServerName != null)
            ByteBufferUtil.putBytes(body, DATA_SERVER_NAME_POS, DataServerName.getBytes(), DATA_SERVER_NAME_LEN);
        body.put(USE_PROXY_POS, (byte) (UseProxy ? 1 : 0));
        if (ProxyServerName != null)
            ByteBufferUtil.putBytes(body, PROXY_SERVER_NAME_POS, ProxyServerName.getBytes(), PROXY_SERVER_NAME_LEN);
        body.putShort(PROXY_SERVER_PORT_POS, ProxyPort);

        return body.array();
    }
}
