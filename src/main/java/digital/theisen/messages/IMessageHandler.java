package digital.theisen.messages;

public interface IMessageHandler {
    boolean handler(IMessage message);
}
