package digital.theisen.sensors;

import digital.theisen.messages.ByteBufferUtil;
import org.apache.commons.codec.binary.Hex;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class SensorPackage implements ISensorPackage {
    private Date timestamp;
    private byte[] deviceId;
    private static final int PACKAGE_SIZE = 64;
    private static final int BITS_6 = 0x7f;
    private static final int BITS_10 = 0x7ff;

    SensorPackage(Date timestamp, byte[] deviceId) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
    }

    public static ISensorPackage[] parseAll(@NonNull byte[] bytes) throws Exception {
        if(bytes.length % PACKAGE_SIZE != 0) {
            throw new Exception("Invalid package size");
        }
        List<ISensorPackage> sensorPackages = new ArrayList<>();

        for(int i = 0; i < bytes.length; i += PACKAGE_SIZE) {
            ISensorPackage sensorPackage = parse(Arrays.copyOfRange(bytes, i, i + 64));
            if(sensorPackage != null) {
                sensorPackages.add(sensorPackage);
            }
        }

        return sensorPackages.toArray(new ISensorPackage[]{});
    }

    @Nullable
    public static ISensorPackage parse(@NonNull byte[] bytes) throws Exception {
        if(bytes.length != PACKAGE_SIZE) {
            throw new Exception("Invalid package size");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);

        byte header = buffer.get(0);
        System.out.println(Hex.encodeHexString(buffer.array()));
        int timestampBytes = buffer.getInt(1);
        Date timestamp = new Date(Integer.toUnsignedLong(timestampBytes) * 1000);
        byte length = buffer.get(5);
        checkIntegrity(buffer);
        byte[] deviceId = ByteBufferUtil.readBytes(buffer, 6, 6);
        byte[] data = ByteBufferUtil.readBytes(buffer, 12, length);

        switch (header) {
            case Constants.MA10100:
                return SensorPackageMA10100.parse(timestamp, deviceId, data);
            default:
                return null;
        }
    }

    private static void checkIntegrity(ByteBuffer buffer) throws Exception {
        byte checksum = buffer.get(63);
        int sum = 0;
        for(byte i=0; i<63; i++) {
            sum += buffer.get(i);
        }
        if((sum & BITS_6) != checksum) {
            throw new Exception("Invalid package checksum in package: " + Hex.encodeHexString(buffer));
        }
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public byte[] getDeviceId() {
        return deviceId;
    }

    static BigDecimal convertTemp(short value) {
        if((value & 0x2000) != 0) return null;
        if((value & 0x1000) != 0) return null;

        BigDecimal absolute = BigDecimal.valueOf(value & BITS_10).divide(BigDecimal.TEN, 1, BigDecimal.ROUND_HALF_UP);
        if((value & 0x400) != 0) {
            return BigDecimal.valueOf(2048).subtract(absolute);
        }

        return absolute;
    }
}
