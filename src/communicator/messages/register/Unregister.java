package communicator.messages.register;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class Unregister extends Message{
    /*
    length UNREG IP_address port_no username

    e.g., 0028 UNREG 64.12.123.190 432 xxxxx
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    UNREG – Unregister request.
    IP_address – IP address in xxx.xxx.xxx.xxx format. This is the IP address other nodes will use to reach you. Indicated with up to 15 characters.
    port_no – Port number. This is the port number that other nodes will connect to. Up to 5 characters.
    username – A string with characters & numbers. Should be the same username used to register the node.
     */
    private String ipAddress;
    private String port;
    private String userName;

    public Unregister(String ipAddress,String port,String userName){
        this.ipAddress=ipAddress;
        this.port=port;
        this.userName=userName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        /*0028 UNREG 64.12.123.190 432 xxxx*/
        String msg=" UNREG "+ipAddress+" "+port+" "+userName;
        msg=String.format("%04d",msg.length())+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=message.split(" ");
        this.ipAddress=s[2];
        this.port=s[3];
        this.userName=s[4];
    }
}
