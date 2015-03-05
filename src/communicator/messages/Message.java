package communicator.messages;

/**
 * Created by lasitha on 3/5/15.
 */
public abstract class Message {
//    private int length;
//
//    public void setLength(int length) {
//        this.length = length;
//    }
//
//    public int getLength() {
//        return length;
//    }
    //

    public abstract String toString();
    public abstract void decodeMessage(String message);
}
