package communicator;

import communicator.messages.Message;

import java.io.IOException;
import java.net.*;

/**
 * Created by ujitha on 3/6/15.
 */
public class UDPClient {

    public UDPClient() {
    }

    public String sendMessage(String destinationIp, int port, Message message) {

        String response = "";
        try {
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(destinationIp);

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];
            String sentence = message.toString();

            sendData = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//            clientSocket.receive(receivePacket);
//            String modifiedSentence = new String(receivePacket.getData());
//            response=modifiedSentence;
//            System.out.println("FROM SERVER:" + modifiedSentence);
            clientSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

//    public static void main(String[] args) {
//        UDPClient udpClient=new UDPClient();
//        udpClient.sendMessage("10.8.108.135",6000,"Hi Ujitha");
//    }
}