package app;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ujitha on 3/5/15.
 */
public class Node {
    private String BSip;
    private int BSport;
    private List<String> connectedNodeList;


    public Node(String BSip,int BSport){
        this.BSip=BSip;
        this.BSport=BSport;
        connectedNodeList=new LinkedList<String>();
    }

    public void start(){
        Socket socket;
        try {
            System.out.println("Connecting to Bootstrap server ...");
            //socket=new Socket(BSip,BSport);
            DatagramSocket DS=new DatagramSocket();
            InetAddress ip=InetAddress.getByName("10.218.47.95");
            DS.connect(ip,5000);
//            OutputStream outToServer=socket.getOutputStream();
//            DataOutputStream out=new DataOutputStream(outToServer);
//            String msg=" REG "+socket.getLocalSocketAddress()+" "+socket.getLocalPort()+" user1";
//            int length=4+msg.length();
//            String len="00"+Integer.toString(length);
//
//            String msg1=len+msg;
//            System.out.println(msg1);
//
//            socket.close();
            System.out.println(Inet4Address.getLocalHost().getHostAddress());

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public static void main(String[] args){
        Node node=new Node("10.218.47.95",5000);
        node.start();

    }
}

