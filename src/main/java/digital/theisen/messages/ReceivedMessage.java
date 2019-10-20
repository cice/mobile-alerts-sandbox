package digital.theisen.messages;

import org.eclipse.jdt.annotation.NonNullByDefault;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@NonNullByDefault
public abstract class ReceivedMessage {
    public static IMessage parse(byte[] buf, int length) {
        MessageHeader header = MessageHeader.parse(buf);
        ByteBuffer body = ByteBuffer.wrap(buf, 0, header.Size);
        body.order(ByteOrder.BIG_ENDIAN);
        if (header.Size > length) throw new IndexOutOfBoundsException();

        switch (header.Command) {
            case GetConfigMessage.COMMAND:
                return parseGetConfig(header, body);
            default:
                return null;
        }
    }

    private static GetConfigMessage parseGetConfig(MessageHeader header, ByteBuffer body) {
        GetConfigMessage getConfigMessage = new GetConfigMessage(header.GatewayId);

        getConfigMessage.DataServerName = ByteBufferUtil.readString(body, GetConfigMessage.DATA_SERVER_NAME_POS, GetConfigMessage.DATA_SERVER_NAME_LEN);
        getConfigMessage.DeviceName = ByteBufferUtil.readString(body, GetConfigMessage.DEVICE_NAME_POS, GetConfigMessage.DEVICE_NAME_LEN);
        getConfigMessage.UseDHCP = body.get(GetConfigMessage.USE_DHCP_POS) != 0;
        getConfigMessage.DHCPIp = ByteBufferUtil.readInetAddress(body, GetConfigMessage.DHCP_IP_POS);
        getConfigMessage.FixedIp = ByteBufferUtil.readInetAddress(body, GetConfigMessage.FIXED_IP_POS);
        getConfigMessage.FixedDNSIp = ByteBufferUtil.readInetAddress(body, GetConfigMessage.FIXED_DNS_POS);
        getConfigMessage.FixedGatewayIp = ByteBufferUtil.readInetAddress(body, GetConfigMessage.FIXED_GATEWAY_POS);
        getConfigMessage.FixedNetMask = ByteBufferUtil.readInetAddress(body, GetConfigMessage.FIXED_NET_MASK_POS);
        getConfigMessage.UseProxy = body.get(GetConfigMessage.USE_PROXY_POS) != 0;
        getConfigMessage.ProxyServerName = ByteBufferUtil.readString(body, GetConfigMessage.PROXY_SERVER_NAME_POS, GetConfigMessage.PROXY_SERVER_NAME_LEN);
        getConfigMessage.ProxyPort = body.getShort(GetConfigMessage.PROXY_SERVER_PORT_POS);

        return getConfigMessage;
    }

}
