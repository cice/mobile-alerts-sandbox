package digital.theisen.messages;

public interface IMessage {
    CommandType getCommandType();

    byte[] getBytes();
}

