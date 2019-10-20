package digital.theisen.messages;

import org.eclipse.jdt.annotation.NonNullByDefault;

@NonNullByDefault
public class FindGatewaysMessage implements IMessage {
    public static short SIZE = 10;
    private static byte[] ALL_GATEWAYS = new byte[]{0, 0, 0, 0, 0, 0};
    private byte[] GatewayId;

    public FindGatewaysMessage() {
        GatewayId = ALL_GATEWAYS;
    }

    public FindGatewaysMessage(byte[] gatewayId) {
        GatewayId = gatewayId;
    }

    public byte[] getGatewayId() {
        return GatewayId;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.FIND_GATEWAYS;
    }

    public byte[] getBytes() {
        MessageHeader header = new MessageHeader(getCommandType().getValue(), GatewayId, SIZE);

        return header.getBytes();
    }
}
