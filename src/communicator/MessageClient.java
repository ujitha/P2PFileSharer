package communicator;

import communicator.messages.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by ujitha on 3/5/15.
 */
public class MessageClient {

    private String myIp;
    private int myPort;
    private Socket clientSocket;

    public MessageClient(String myIp,int myPort){
        this.myIp=myIp;
        this.myPort=myPort;
    }

    public MessageClient(){}

    private void connect(String ip,int port) throws IOException {
        System.out.println("client attempting to connect to the server "+ip+" on port "+port);
        clientSocket=new Socket(ip,port);
        System.out.println("Connected to " + ip + " on " + port);
    }

    private String readResponse() throws IOException {
        String userInput;
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Response from server:");
        while ((userInput = stdIn.readLine()) != null) {
            System.out.println(userInput);
        }

        return userInput;
    }

    public String sendMessage(String destinationIp,int port,Message message) throws IOException {
          connect(destinationIp,port);
          OutputStream outToServer = clientSocket.getOutputStream();
          PrintStream out = new PrintStream(outToServer);
          out.print(message.toString());
          String receivedMessage=readResponse();
          clientSocket.close();
        return receivedMessage;
    }

}
