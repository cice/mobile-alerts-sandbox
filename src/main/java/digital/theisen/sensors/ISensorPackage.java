package digital.theisen.sensors;

import java.util.Date;

public interface ISensorPackage {
    Date getTimestamp();
    byte[] getDeviceId();
}
