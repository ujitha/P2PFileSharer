package communicator.messages.join;

import communicator.messages.Message;

import java.util.StringTokenizer;


/**
 * Created by lasitha on 3/5/15.
 */
public class AckJoin extends Message{
    /*
    length JOINOK value ip port

    e.g., 0014 JOINOK 0 10.10.10.10 1000
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    JOINOK – Join response.
    value – Indicate success or failure
        0 – successful
        9999 – error while adding new node to routing table
    ip - ip address of the acknow
    port - port of ack
     */

    private int value;
    private String ip;
    private String port;

    public AckJoin(int value, String ip, String port) {
        this.value = value;
        this.ip = ip;
        this.port = port;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        /*0014 JOINOK 0*/
        String msg=" JOINOK "+value+" "+ip+" "+port;
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
