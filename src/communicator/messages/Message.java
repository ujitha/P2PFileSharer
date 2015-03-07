package communicator.messages;

/**
 * Created by lasitha on 3/5/15.
 */
public abstract class Message {
    private int length;

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public abstract String toString();
    public abstract void decodeMessage(String message);

    public String[] splitMessage(String message){
        message=message.trim();
        this.setLength(Integer.parseInt(message.split(" ")[0]));
        message=message.substring(0,this.getLength());
        String[] s=message.split(" ");
        return s;
    }
}
