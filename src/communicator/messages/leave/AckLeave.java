package communicator.messages.leave;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckLeave extends Message {
    /*
    length LEAVEOK value

    e.g., 0014 LEAVEOK 0
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    LEAVEOK – Leave response.
    value – Indicate success or failure
        If 0 – successful, if 9999 – error while adding new node to routing table
     */

    private int value;

    public AckLeave(int value){
        this.value=value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        /*0014 LEAVEOK 0*/
        String msg=" LEAVEOK "+value;
        msg=String.format("%04d",msg.length())+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String s=message.split(" ")[2];
        this.value=Integer.parseInt(s);
    }
}
