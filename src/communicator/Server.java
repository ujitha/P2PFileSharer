package communicator;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ujitha on 3/5/15.
 */
public class Server extends Thread{

    private String myIp;
    private int myDefaultPort;
    private ServerSocket serverSocket;
    private MessageCallback messageCallback;

    public Server(String myIp,int port,MessageCallback messageCallback){
        this.myIp=myIp;
        this.myDefaultPort=port;
        this.messageCallback=messageCallback;
    }

    public void run(){

        try {
            serverSocket=new ServerSocket(myDefaultPort);
            System.out.println("Server is listening on port : "+myDefaultPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                Socket server=serverSocket.accept();
                DataInputStream in =new DataInputStream(server.getInputStream());
                String msg=in.readUTF();
                System.out.println(msg);
                messageCallback.receiveMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void disconnectServer(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
