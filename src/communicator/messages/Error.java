package communicator.messages;

/**
 * Created by lasitha on 3/5/15.
 */
public class Error extends Message {
    /*
    length ERROR

    0010 ERROR
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    ERROR – Generic error message, to indicate that a given command is not understood. For storing and searching files/keys this should be send to the initiator of the message.
     */

    private String error;

    public Error(){
        this.error=null;
    }
    public Error(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        String msg=" ERROR";
        if(error!=null) {
            msg = msg + " " + error;
        }
        msg=String.format("%04d",msg.length())+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=message.split(" ");
        if(s.length>2) {
            int index=message.indexOf(s[1]);
            this.error = message.substring(index+s[1].length());
        }
    }
}
