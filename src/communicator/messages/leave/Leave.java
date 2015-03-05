package communicator.messages.leave;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class Leave extends Message{

    /*
     length LEAVE IP_address port_no

    e.g., 0028 LEAVE 64.12.123.190 432
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    LEAVE – Leave request.
    IP_address – IP address in xxx.xxx.xxx.xxx format. This is the IP address other nodes will use to reach you. Indicated with up to 15 characters.
    port_no – Port number. This is the port number that other nodes will connect to. Up to 5 characters.
     */
    private String ipAddress;
    private String port;

    public Leave(String ipAddress,String port){
        this.ipAddress=ipAddress;
        this.port=port;
    }

    public Leave(String message){
        this.decodeMessage(message);
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        /*0028 LEAVE 64.12.123.190 432*/
        String msg=" LEAVE "+ipAddress+" "+port;
        msg=String.format("%04d",msg.length())+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String s[]=message.split(" ");
        this.ipAddress=s[2];
        this.port=s[3];
    }
}
