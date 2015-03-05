package app;

import communicator.MessageClient;
import communicator.messages.Message;
import communicator.messages.register.Register;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.*;
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
        Message registerMsg=new Register("10.216.41.163","6000","user1");
        Message registerMsg2=new Register("10.216.41.163","7000","user2");
        MessageClient messageClient=new MessageClient();

        try {
            messageClient.sendMessage(BSip,BSport,registerMsg);
            messageClient.sendMessage(BSip,BSport,registerMsg2);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    private void TCPconnectionStart(){
//        Socket client;
//        try {
//            client=new Socket(BSip,BSport);
//
//            OutputStream outToServer=client.getOutputStream();
//            PrintStream out = new PrintStream(outToServer);
//
//
//            String msg=" REG "+BSip+" 6000"+" user1";
//            int length=4+msg.length();
//            String len="00"+Integer.toString(length);
//
//            String msg1=len+msg;
//            System.out.println(msg1);
//            out.print(msg1);
//
//
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    private void UDPtransportStart() throws UnknownHostException {
//        DatagramSocket client;
//        try {
//            client = new DatagramSocket();
//            InetAddress ip=InetAddress.getByName(BSip);
//            client.connect(ip, 5000);
//
//            String msg=" REG "+BSip+" "+"6000"+" user1";
//            int length=4+msg.length();
//            String len="00"+Integer.toString(length);
//            String msg1=len+msg;
//
//            DatagramPacket dp=new DatagramPacket(msg1.getBytes(),msg1.length(),ip,BSport);
//            client.send(dp);
//
//
//            DatagramSocket ds=new DatagramSocket(4000);
//            byte[] buf = new byte[1024];
//            DatagramPacket dp1 = new DatagramPacket(buf, 1024);
//            ds.receive(dp1);
//            String str = new String(dp1.getData(), 0, dp1.getLength());
//            System.out.println(str);
//
//
//            client.close();
//
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public static void main(String[] args){
        Node node=new Node("10.216.41.163",5000);
        node.start();

    }
}

