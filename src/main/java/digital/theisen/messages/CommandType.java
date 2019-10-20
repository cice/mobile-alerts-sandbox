package digital.theisen.messages;

public enum CommandType {
    GET_CONFIG(3),
    SET_CONFIG(4),
    FIND_GATEWAYS(1);

    private final short value;

    CommandType(int value) {
        this.value = (short) value;
    }

    public short getValue() {
        return value;
    }
}
