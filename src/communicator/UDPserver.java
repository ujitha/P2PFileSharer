package communicator;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by visiri on 3/6/15.
 */
public class UDPserver extends Thread {

    private String myIp;
    private int myDefaultPort;
    private MessageCallback messageCallback;
    DatagramSocket serverSocket;

    public UDPserver(String myIp,int port,MessageCallback messageCallback){
        this.myIp=myIp;
        this.myDefaultPort=port;
        this.messageCallback=messageCallback;
    }

    public void run(){
        try {
            serverSocket = new DatagramSocket(myDefaultPort);
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            while (true){

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String( receivePacket.getData());
                System.out.println("RECEIVED: " + sentence);
                messageCallback.receiveMessage(sentence);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args){
//        UDPserver udPserver=new UDPserver("10.8.108.135",6000,new MessageCallback() {
//            @Override
//            public void receiveMessage(String message) {
//                System.out.println(message);
//            }
//        });
//        udPserver.start();
//    }
}
