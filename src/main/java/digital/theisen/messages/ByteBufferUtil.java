package digital.theisen.messages;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ByteBufferUtil {
    static Inet4Address readInetAddress(ByteBuffer body, int index) {
        byte[] buf = readBytes(body, index, 4, false);
        try {
            return (Inet4Address) Inet4Address.getByAddress(buf);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    static String readString(ByteBuffer body, int index, int length) {
        return new String(readBytes(body, index, length, true));
    }

    public static byte[] readBytes(ByteBuffer body, int index, int length) {
        return readBytes(body, index, length, false);
    }

    public static byte[] readBytes(ByteBuffer body, int index, int length, boolean cString) {
        byte[] buf = new byte[length];
        int actualLength = length;
        for (int i = 0; i < length; i++) {
            buf[i] = body.get(i + index);
            if (cString && buf[i] == 0) {
                actualLength = i;
                break;
            }
        }
        return Arrays.copyOfRange(buf, 0, actualLength);
    }

    static void putBytes(ByteBuffer buffer, int index, byte[] bytes, int length) {
        int i = index;
        for (byte aByte : bytes) {
            if (i >= length + index) return;
            buffer.put(i++, aByte);
        }
    }
}
