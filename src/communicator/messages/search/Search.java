package communicator.messages.search;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class Search extends Message {
    /*
    length SER IP port file_name hops

    e.g., Suppose we are searching for Lord of the rings, 0047 SER 129.82.62.142 5070 "Lord of the rings"
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
            SER – Locate a file with this name.
            IP – IP address of the node that is searching for the file. May be useful depending your design.
    port – port number of the node that is searching for the file. May be useful depending your design.
    file_name – File name being searched.
    hops – A hop count. May be of use for cost calculations (optional).
    */
    private String ip;
    private String port;
    private String fileName;
    private int hops;

    public Search(String ip, String port, String fileName,int hops){
        this.ip=ip;
        this.port=port;
        this.fileName=fileName;
        this.hops=hops;
    }

    public Search(String message){
        this.decodeMessage(message);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public int reduceHopCount(){
        return --hops;
    }
    public int increaseHopCount(){
        return ++hops;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getFileName() {
        return fileName;
    }

    public int getHops() {
        return hops;
    }

    @Override
    public String toString() {
        /*length SER IP port file_name hops*/
        String msg=" SER "+ip+" "+port+" "+fileName+" "+hops;
        msg=String.format("%04d",msg.length()+4)+msg;
        return msg;
    }

    @Override
    public void decodeMessage(String message) {
        String[] s=message.split(" ");
        this.ip=s[2];
        this.port=s[3];
        this.fileName=message.split("\"(.*?)\"")[0];
        this.hops=Integer.parseInt(s[5]);
    }
}
