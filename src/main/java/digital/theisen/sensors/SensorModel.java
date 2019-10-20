package digital.theisen.sensors;

public enum SensorModel {
    MA10100(0xCE, 0x02),
    MA10101(0xCE, 0x02);

    private final byte packageHeader;
    private final byte deviceId;

    SensorModel(int packageHeader, int deviceId) {
        this.packageHeader = (byte) packageHeader;
        this.deviceId = (byte) deviceId;
    }

    public byte getDeviceId() {
        return deviceId;
    }

    public byte getPackageHeader() {
        return packageHeader;
    }
}
