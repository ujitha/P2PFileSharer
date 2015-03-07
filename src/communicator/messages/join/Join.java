package communicator.messages.join;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class Join extends Message{
    /*
     length JOIN IP_address port_no

    e.g., 0027 JOIN 64.12.123.190 432
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    JOIN – Join request.
    IP_address – IP address in xxx.xxx.xxx.xxx format. This is the IP address other nodes will use to reach you. Indicated with up to 15 characters.
    port_no – Port number. This is the port number that other nodes will connect to. Up to 5 characters.
     */
    private String ip;
    private String port;
    private String userName;

    public Join(String ip,String port,String userName){
        this.ip=ip;
        this.port=port;
        this.userName=userName;
    }


    public Join(String message){
        this.decodeMessage(message);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }



    @Override
    public String toString() {
        /*0027 JOIN 64.12.123.190 432*/
        String msg=" JOIN "+ip+" "+port+" "+userName;
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=splitMessage(message);
        this.ip=s[2];
        this.port=s[3];
        this.userName=s[4];
    }
}
