package digital.theisen.sensors;

import java.math.BigDecimal;
import java.util.Date;

public class SensorPackageMA10101 extends SensorPackageMA10100 {
    public SensorPackageMA10101(Date timestamp, byte[] deviceId, short txCounter, BigDecimal currentTemp, BigDecimal prevTemp) {
        super(timestamp, deviceId, txCounter, currentTemp, prevTemp);
    }
}
