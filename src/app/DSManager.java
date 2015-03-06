/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import communicator.MessageCallback;
import communicator.MessageClient;
import communicator.Server;
import communicator.messages.Message;
import communicator.messages.MessageDecoder;
import communicator.messages.register.AckRegister;
import communicator.messages.register.Register;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Pubudu
 */
public class DSManager {


    public String bootStrapIp;
    public int bootStrapPort;
    public Node node;
    public FileRepo fileRepo;
    private int MY_DEFAUILT_PORT=6000;
    private List<Node> connectedNodeList;



    public DSManager(String bootStrapIp,int bootStrapPort,String myIp){
        this.bootStrapIp=bootStrapIp;
        this.bootStrapPort=bootStrapPort;

        String username="user:"+myIp;
        node=new Node(myIp,MY_DEFAUILT_PORT,username);
        fileRepo=new FileRepo();
        addFilesToNode();
        this.connectedNodeList=new ArrayList<Node>();
    }

    public DSManager(String bootStrapIp,int bootStrapPort,String myIp,int myPort){
        this.bootStrapIp=bootStrapIp;
        this.bootStrapPort=bootStrapPort;

        String username="user:"+myIp;
        node=new Node(myIp,myPort,username);
        fileRepo=new FileRepo();
        addFilesToNode();
        this.connectedNodeList=new ArrayList<Node>();
    }


    public void start(){
        this.connectToBS();
        Server server=new Server(node.getMyIp(),node.getMyDefaultPort(),new MessageCallback() {
            @Override
            public void receiveMessage(String message) {

            }
        });

        server.start();

    }

    private void connectToBS(){
        Message registerMsg=new Register(node.getMyIp(),Integer.toString(node.getMyDefaultPort()),node.getMyUsername());
        MessageClient messageClient=new MessageClient();

        try {
            String receivedMessage=messageClient.sendMessage(bootStrapIp,bootStrapPort,registerMsg);
            MessageDecoder messageDecoder=new MessageDecoder();
            Message message=messageDecoder.decodeMessage(receivedMessage);

            if(message instanceof AckRegister){
                AckRegister ackRegister=(AckRegister)message;
                if(ackRegister.getNoNodes()>0){
                    Node node1=new Node(ackRegister.getIp1(),Integer.parseInt(ackRegister.getPort1()),ackRegister.getUserName1());
                    connectedNodeList.add(node1);
                    if(ackRegister.getNoNodes()==2){
                        Node node2=new Node(ackRegister.getIp2(),Integer.parseInt(ackRegister.getPort2()),ackRegister.getUserName2());
                        connectedNodeList.add(node2);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFilesToNode() {
        node.addFileList(fileRepo.getFilesFromRepo());
    }

    // check from node's file list, otherwise send to other nodes
    public ArrayList<String> getQueryResults(String query) {

        boolean hasFile = false;
        ArrayList<String> results = node.isFileInRepo(query);
        if (results.isEmpty()) {
            //randomly check two nodes and send their results.
            return results;
        } else {
            return results;
        }

    }

}
