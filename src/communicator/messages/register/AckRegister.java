package communicator.messages.register;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckRegister extends Message {
    /*
     length REGOK no_nodes IP_1 port_1 user1 IP_2 port_2 user2

    e.g., 0059 REGOK 2 129.82.123.45 5001 abc 64.12.123.190 34001 pqr
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
        if(noNodes>=1 && noNodes<9996){
            msg=msg+" "+ip1+" "+port1+" "+userName1;
        }
        if(noNodes>=2 && noNodes<9996){
            msg=msg+" "+ip2+" "+port2+" "+userName2;
        }
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=splitMessage(message);
        this.noNodes=Integer.parseInt(s[2]);
        if(noNodes==1){
            ip1=s[3];
            port1=s[4];
            userName1=s[5];
        }
        //length REGOK no_nodes IP_1 port_1 u1 IP_2 port_2 u2 ip_3 port_3 u3 ...
        if(noNodes>=2 && noNodes<9996){
            int selectedIndex1=3,selectedIndex2=3;
            int num=(int)(Math.random()*100)%noNodes;
            selectedIndex1+=num*3;
            do{
                num=(int)(Math.random()*100)%noNodes;
                selectedIndex2+=num*3;
                if(selectedIndex1!=selectedIndex2){
                    break;
                }
                selectedIndex2=3;
            }while (true);
            ip1=s[selectedIndex1];
            port1=s[selectedIndex1+1];
            userName1=s[selectedIndex1+2];

            ip2=s[selectedIndex2];
            port2=s[selectedIndex2+1];
            userName2=s[selectedIndex2+2];
            noNodes=2;
        }
    }


    public static void main(String[] args) {
        String s= //"0106 REGOK 4 10.8.98.40 5100 .40510 10.8.98.45 5100 .45510 10.8.108.235 5100 235510 10.8.98.24. 5100 .24510";
        "0059 REGOK 2 129.82.123.45 5001 abc 64.12.123.190 34001 pqr";
        //"0095 REGOK 4 129.82.123.45 5001 abc 64.12.123.190 34001 pqr 1.1.1.1 1111 abc2 1.1.1.1 1111 abc3";
        AckRegister a=new AckRegister(s);
        System.out.println(a.toString());
    }
}
