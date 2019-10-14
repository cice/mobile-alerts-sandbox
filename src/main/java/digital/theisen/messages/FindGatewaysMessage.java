package digital.theisen.messages;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class FindGatewaysMessage implements IMessage {
    public static short SIZE = 10;
    private static byte[] ALL_GATEWAYS = new byte[]{0, 0, 0, 0, 0, 0};
    public static short COMMAND = 1;

    public FindGatewaysMessage() {
        GatewayId = ALL_GATEWAYS;
    }

    private byte[] GatewayId;
    public byte[] getGatewayId() {
        return GatewayId;
    }

    public void setGatewayId(byte[] gatewayId) {
        GatewayId = gatewayId;
    }

    public byte[] getBytes() {
        MessageHeader header = new MessageHeader(COMMAND, GatewayId, SIZE);

        return header.getBytes();
    }
}
