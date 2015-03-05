package communicator.messages.register;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class Register extends Message{

    /*
    e.g., 0036 REG 129.82.123.45 5001 1234abcd
    length – Length of the entire message including 4 characters used to indicate the length. Always give length in xxxx format to make it easy to determine the length of the message.
            REG – Registration request.
    IP_address – IP address in xxx.xxx.xxx.xxx format. This is the IP address other nodes will use to reach you. Indicated with up to 15 characters.
            port_no – Port number. This is the port number that other nodes will connect to. Up to 5 characters.
            Username – A string with characters and numbers.
    */
    private String ipAddress;
    private String port;
    private String userName;

    public Register(String ipAddress,String port,String userName){
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
        /*0036 REG 129.82.123.45 5001 1234abcd*/
        String msg=" REG "+ipAddress+" "+port+" "+userName;
        int length=msg.length()+5;
        msg=String.format("%04d", length)+msg;
        return msg;
    }
}
