package communicator.messages.register;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckRegister extends Message {
    /*
     length REGOK no_nodes IP_1 port_1 IP_2 port_2

    e.g., 0051 REGOK 2 129.82.123.45 5001 64.12.123.190 34001
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    REGOK – Registration response.
    no_ nodes – Number of node entries that are going to be returned by the registry
        0 – request is successful, no nodes in the system
        1 or 2 – request is successful, 1 or 2 nodes' contacts will be returned
        9999 – failed, there is some error in the command
        9998 – failed, already registered to you, unregister first
        9997 – failed, registered to another user, try a different IP and port
        9996 – failed, can’t register. BS full.
    IP_1 – IP address of the 1st node (if available).
    port_1 – Port number of the 1st node (if available).
    IP_2 – IP address of the 2nd node (if available).
    port_2 – Port number of the 2nd node (if available).
     */

    private int noNodes;
    private String ip1;
    private String port1;
    private String ip2;
    private String port2;
    private String userName1;
    private String userName2;

    public AckRegister(int noNodes){
        this.noNodes=noNodes;
    }

    public String getUserName1() {
        return userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public AckRegister(int noNodes,String ip1,String port1){
        this.noNodes=noNodes;
        this.ip1=ip1;

        this.port1=port1;
    }

    public AckRegister(int noNodes,String ip1,String port1,String ip2,String port2){
        this.noNodes=noNodes;
        this.ip1=ip1;
        this.port1=port1;
        this.ip2=ip2;
        this.port2=port2;
    }

    public AckRegister(String message){
        this.decodeMessage(message);
    }

    public void setNoNodes(int noNodes) {
        this.noNodes = noNodes;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }

    public void setPort1(String port1) {
        this.port1 = port1;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2;
    }

    public void setPort2(String port2) {
        this.port2 = port2;
    }

    public int getNoNodes() {
        return noNodes;
    }

    public String getIp1() {
        return ip1;
    }

    public String getPort1() {
        return port1;
    }

    public String getIp2() {
        return ip2;
    }

    public String getPort2() {
        return port2;
    }

    @Override
    public String toString() {
        /*length REGOK no_nodes IP_1 port_1 IP_2 port_2  */
        String msg=" REGOK "+noNodes;
        if(noNodes==1 || noNodes==2){
            msg=msg+" "+ip1+" "+port1+" "+userName1;
        }else if(noNodes==2){
            msg=msg+" "+ip2+" "+port2+" "+userName2;
        }
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=message.split(" ");
        this.noNodes=Integer.parseInt(s[2]);
        if(noNodes==1 || noNodes==2){
            ip1=s[3];
            port1=s[4];
            userName1=s[5];
        }
        if(noNodes==2){
            ip2=s[6];
            port2=s[7];
            userName2=s[8];
        }
    }
}
