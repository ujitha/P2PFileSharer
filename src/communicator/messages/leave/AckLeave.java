package communicator.messages.leave;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckLeave extends Message {
    /*
    length LEAVEOK value ip port

    e.g., 0014 LEAVEOK 0 1.1.1.1 1000
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    LEAVEOK – Leave response.
    value – Indicate success or failure
        If 0 – successful, if 9999 – error while adding new node to routing table
    ip - ip of the ack
    port - port of ack
     */
    private int value;
    private String ip;
    private String port;

    public AckLeave(int value, String ip, String port) {
        this.value = value;
        this.ip = ip;
        this.port = port;
    }

    public AckLeave(String message){
        this.decodeMessage(message);
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
        String msg=" LEAVEOK "+value+" "+ip+" "+port;
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=splitMessage(message);
        this.value=Integer.parseInt(s[2]);
        this.ip=s[3];
        this.port=s[4];
    }
}
