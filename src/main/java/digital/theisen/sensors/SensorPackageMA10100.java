package digital.theisen.sensors;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

public class SensorPackageMA10100 extends SensorPackage {

    private final short txCounter;
    private final BigDecimal currentTemp;
    private final BigDecimal prevTemp;

    public SensorPackageMA10100(Date timestamp, byte[] deviceId, short txCounter, BigDecimal currentTemp, BigDecimal prevTemp) {
        super(timestamp, deviceId);
        this.txCounter = txCounter;
        this.currentTemp = currentTemp;
        this.prevTemp = prevTemp;
    }

    public static ISensorPackage parse(Date timestamp, byte[] deviceId, byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.BIG_ENDIAN);

        short txCounter = buffer.getShort(0);
        short currentTemp = buffer.getShort(2);
        short prevTemp = buffer.getShort(4);

        return new SensorPackageMA10100(timestamp, deviceId, txCounter, convertTemp(currentTemp), convertTemp(prevTemp));
    }

    public short getTxCounter() {
        return txCounter;
    }

    public BigDecimal getCurrentTemp() {
        return currentTemp;
    }

    public BigDecimal getPrevTemp() {
        return prevTemp;
    }
}
