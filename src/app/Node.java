package app;

import communicator.MessageCallback;
import communicator.MessageClient;
import communicator.Server;
import communicator.messages.Message;
import communicator.messages.register.Register;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ujitha on 3/5/15.
 */
public class Node {
    private String BSip;
    private int BSport;
    private List<String> connectedNodeList;

    private String myIp;
    private int myDefaultPort;
    private String myUsername;

    public Node(String BSip,int BSport,String myIp,int myDefaultPort,String myUsername){
        this.BSip=BSip;
        this.BSport=BSport;
        this.myIp=myIp;
        this.myDefaultPort=myDefaultPort;
        this.myUsername=myUsername;
        connectedNodeList=new LinkedList<String>();
    }

    public void start(){
       this.connectToBS();
       Server server=new Server(myIp,myDefaultPort,new MessageCallback() {
            @Override
            public void receiveMessage(String message) {

            }
       });

       server.start();

    }

    private void connectToBS(){
        Message registerMsg=new Register(myIp,Integer.toString(myDefaultPort),myUsername);
        MessageClient messageClient=new MessageClient();

        try {
            messageClient.sendMessage(BSip,BSport,registerMsg);
            //TODO has to decode the received message and extract the node IDs
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

    }
}

