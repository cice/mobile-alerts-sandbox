package digital.theisen.messages;

public interface IMessage {
    byte[] getGatewayId();
    void setGatewayId(byte[] gatewayId);
    byte[] getBytes();
}
