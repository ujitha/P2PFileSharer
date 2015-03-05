package communicator.messages.join;

import communicator.messages.Message;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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

    public Join(String ip,String port){
        this.ip=ip;
        this.port=port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    @Override
    public String toString() {
        throw new NotImplementedException();
    }
}