package communicator.messages;

/**
 * Created by lasitha on 3/5/15.
 */
public abstract class Message {
    public abstract String toString();
    public abstract void decodeMessage(String message);
}
