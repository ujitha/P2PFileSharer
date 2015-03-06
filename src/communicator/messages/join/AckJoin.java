package communicator.messages.join;

import communicator.messages.Message;

import java.util.StringTokenizer;


/**
 * Created by lasitha on 3/5/15.
 */
public class AckJoin extends Message{
    /*
    length JOINOK value

    e.g., 0014 JOINOK 0
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    JOINOK – Join response.
    value – Indicate success or failure
        0 – successful
        9999 – error while adding new node to routing table
     */
    private int value;

    public AckJoin(int value){
        this.value=value;
    }

    public AckJoin(String message){
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
        /*0014 JOINOK 0*/
        String msg=" JOINOK "+value;
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String s=message.split(" ")[2];
        this.value=Integer.parseInt(s);
    }
}
