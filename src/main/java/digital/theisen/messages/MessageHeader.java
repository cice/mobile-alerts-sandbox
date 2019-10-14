package digital.theisen.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MessageHeader {
    public static final int HEADER_SIZE = 10;
    private static final int COMMAND_POS = 0;
    private static final int GATEWAY_ID_POS = 2;
    private static final int GATEWAY_ID_LEN = 6;
    private static final int SIZE_POS = 8;
    private static final int SIZE_LEN = 2;

    short Command;
    byte[] GatewayId;
    short Size;

    public MessageHeader(short command, byte[] gatewayId, short size) {
        Command = command;
        GatewayId = gatewayId;
        Size = size;
    }

    public static MessageHeader parse(byte[] buf) {
        if (buf.length < HEADER_SIZE) throw new IllegalArgumentException("buf must at least have 10 bytes");
        ByteBuffer buffer = ByteBuffer.wrap(buf);
        buffer.order(ByteOrder.BIG_ENDIAN);

        short command = buffer.getShort(COMMAND_POS);
        byte[] gatewayId = new byte[GATEWAY_ID_LEN];
        for (int i = 0; i < GATEWAY_ID_LEN; i++) {
            gatewayId[i] = buffer.get(i + GATEWAY_ID_POS);
        }
        short size = buffer.getShort(SIZE_POS);
        return new MessageHeader(command, gatewayId, size);
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE);
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.putShort(COMMAND_POS, Command);
        ByteBufferUtil.putBytes(buffer, GATEWAY_ID_POS, GatewayId, GATEWAY_ID_LEN);
        buffer.putShort(SIZE_POS, Size);

        return buffer.array();
    }

}
