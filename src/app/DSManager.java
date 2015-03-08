/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import communicator.*;
import communicator.messages.Message;
import communicator.messages.MessageDecoder;
import communicator.messages.join.AckJoin;
import communicator.messages.join.Join;
import communicator.messages.leave.AckLeave;
import communicator.messages.leave.Leave;
import communicator.messages.register.AckRegister;
import communicator.messages.register.Register;
import communicator.messages.register.Unregister;
import communicator.messages.search.AckSearch;
import communicator.messages.search.Search;
import javafx.application.Platform;
import view.FSViewController;

import java.io.IOException;
import java.util.*;

/**
 * @author Pubudu
 */
public class DSManager {

    public String bootStrapIp;
    public int bootStrapPort;
    public Node node;
    public FileRepo fileRepo;
    private int MY_DEFAUILT_PORT = 6000;
    private int TIMER_SECONDS = 5;
    private List<Node> connectedNodeList;
    private UDPserver server;
    private int TOTAL_HOP_COUNT = 8;
    private boolean isTimerOn = false;
    private HashMap<String, String[]> queryResults;
    private FSViewController controller;
    private Timer searchTimer;

    // Constructor for having default port number
    public DSManager(String bootStrapIp, int bootStrapPort, String myIp, FSViewController controller) {
        this.bootStrapIp = bootStrapIp;
        this.bootStrapPort = bootStrapPort;
        this.controller = controller;

        String username = "user:" + myIp;
        node = new Node(myIp, MY_DEFAUILT_PORT, username);
        fileRepo = new FileRepo();
        addFilesToNode();
        this.connectedNodeList = new ArrayList<Node>();
    }

    // Constructor for having an overriding port number
    public DSManager(String bootStrapIp, int bootStrapPort, String myIp, int myPort, FSViewController controller) {
        this.bootStrapIp = bootStrapIp;
        this.bootStrapPort = bootStrapPort;
        this.controller = controller;

        String username = "user:" + myIp + myPort;
        username = username.substring(username.length() - 7, username.length());
        node = new Node(myIp, myPort, username);
        fileRepo = new FileRepo();
        addFilesToNode();
        this.connectedNodeList = new ArrayList<Node>();
    }

    // Calls from UI when leaving the network to inform other nodes
    public void sendLeaveMessages() {

        Message unregMessage = new Unregister(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), node.getMyUsername());
        MessageClient messageClient = new MessageClient();

        try {
            messageClient.sendMessage(bootStrapIp, bootStrapPort, unregMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < connectedNodeList.size(); i++) {

            String nodeIp = connectedNodeList.get(i).getMyIp();
            String nodePort = Integer.toString(connectedNodeList.get(i).getMyDefaultPort());
            Message leaveMsg = new Leave(node.getMyIp(), Integer.toString(node.getMyDefaultPort()));
            UDPClient udpClient = new UDPClient();
            udpClient.sendMessage(nodeIp, Integer.parseInt(nodePort), leaveMsg);

        }
        server.closeSocket();
    }

    // Returns node's file list
    public ArrayList<String> getNodeFileList() {
        return this.node.getFiles();
    }

    // Call from UI to connect
    public String start() {

        server = new UDPserver(node.getMyIp(), node.getMyDefaultPort(), new MessageCallback() {
            @Override
            public void receiveMessage(String message) {

                controller.writeToLog(message);
                MessageDecoder msDecoder = new MessageDecoder();
                Message incomingMsg = msDecoder.decodeMessage(message);

                if (incomingMsg instanceof Join) {
                    Join joinNode = (Join) incomingMsg;
                    processJoinMsg(joinNode);

                } else if (incomingMsg instanceof AckLeave) {
                    AckLeave leaveMsg = (AckLeave) incomingMsg;
                    int leaveValue = leaveMsg.getValue();
                    if (leaveValue == 0) {
                        //ok to leave
                    } else {
                        // not ok to leave
                    }
                } else if (incomingMsg instanceof Leave) {
                    Leave leaveMsg = (Leave) incomingMsg;
                    processLeaveMsg(leaveMsg);

                } else if (incomingMsg instanceof Search) {
                    Search searchMsg = (Search) incomingMsg;
                    processSearchMsg(searchMsg);

                } else if (incomingMsg instanceof AckSearch) {

                    AckSearch searchAck = (AckSearch) incomingMsg;

                    if (isTimerOn) {
                        int ackValue = searchAck.getNoOfFiles();
                        stopTimer();

                        if (ackValue > 0) {
                            String ip = searchAck.getIp();
                            String[] results = searchAck.getFileNames();
                            queryResults.put(ip, results);
                        }

                        startTimer();
                    }

                } else if (incomingMsg instanceof AckJoin) {
                    AckJoin ackJoin = (AckJoin) incomingMsg;
                    controller.writeToLog(ackJoin.toString());
                    //to change to get ip from meta data

                    //  AckJoin ackJoin = (AckJoin) incomingMsg;
                    // if (ackJoin.getValue() == 9999) {
                    // }


                }


            }
        });

        String connectResult = this.connectToBS();
        return connectResult;

    }

    // Process Join Message
    private void processJoinMsg(Join joinNode) {

        String ip = joinNode.getIp();
        String port = joinNode.getPort();
        String userName = joinNode.getUserName();
        Node node = new Node(ip, Integer.parseInt(port), userName);
        boolean insertInTable = addNodeToList(node);
        int resValue = 0;
        if (insertInTable) {
            resValue = 9999;
        }

        Message joinAck = new AckJoin(resValue);
        UDPClient messageClient = new UDPClient();

        messageClient.sendMessage(ip, Integer.parseInt(port), joinAck);
    }

    // Process Search Message
    private void processSearchMsg(Search searchMsg) {

        int hopSize = searchMsg.getHops();
        String ip = searchMsg.getIp();
        String port = searchMsg.getPort();
        String fileName = searchMsg.getFileName();
        ArrayList<String> results = getNodeQueryResults(fileName);

        //if the search request is sent by myself, ignore it
        if(ip.equals(node.getMyIp()) && port.equals(Integer.toString(node.getMyDefaultPort()))){
            return;
        }

        if (!results.isEmpty()) {

            Message searchAck = new AckSearch(results.size(), node.getMyIp(), Integer.toString(node.getMyDefaultPort()), TOTAL_HOP_COUNT - hopSize, results.toArray(new String[results.size()]));
            UDPClient messageClient = new UDPClient();
            messageClient.sendMessage(ip, Integer.parseInt(port), searchAck);

        } else {

            if (hopSize == 0) {
                Message searchAck = new AckSearch(0, node.getMyIp(), Integer.toString(node.getMyDefaultPort()), TOTAL_HOP_COUNT - hopSize, results.toArray(new String[results.size()]));
                UDPClient messageClient = new UDPClient();
                messageClient.sendMessage(ip, Integer.parseInt(port), searchAck);

            } else {
                Random random = new Random();
                int nodeSize = connectedNodeList.size();
                int nodeCount = 2;       // select two nodes to send
                if (nodeSize == 1) {
                    nodeCount = 1;
                }

                ArrayList<Integer> sentNodes = new ArrayList<Integer>();
                while (nodeCount > 0) {
                    int nodeId = random.nextInt(nodeSize);
                    boolean hasId = false;
                    for (int i = 0; i < sentNodes.size(); i++) {
                        if (nodeId == sentNodes.get(i)) {
                            hasId = true;
                            break;
                        }
                    }

                    if (!hasId) {
                        String nodeIp = connectedNodeList.get(nodeId).getMyIp();
                        int nodePort = connectedNodeList.get(nodeId).getMyDefaultPort();
                        searchMsg.reduceHopCount();
                        UDPClient messageClient = new UDPClient();
                        messageClient.sendMessage(nodeIp, nodePort, searchMsg);
                        nodeCount--;
                    }
                }
            }
        }
    }

    // Process Leave Message
    private void processLeaveMsg(Leave leaveMsg) {

        String nodeIp = leaveMsg.getIpAddress();
        String nodePort = leaveMsg.getPort();
        int leaveValue = 9999;

        for (int i = 0; i < connectedNodeList.size(); i++) {
            Node n = connectedNodeList.get(i);
            if ((n.getMyIp().equals(nodeIp))&&(n.getMyDefaultPort()==Integer.parseInt(nodePort))) {
                leaveValue = 0;
                controller.removeNeighbour(connectedNodeList.get(i));
                connectedNodeList.remove(i);
                break;
            }
        }

        Message leaveAck = new AckLeave(leaveValue);
        UDPClient messageClient = new UDPClient();

        messageClient.sendMessage(nodeIp, Integer.parseInt(nodePort), leaveAck);
    }

    private boolean addNodeToList(Node node) {
        boolean hasNode = false;
        for (int i = 0; i < connectedNodeList.size(); i++) {
            Node n = connectedNodeList.get(i);
            if ((n.getMyIp().equals(node.getMyIp()))&&(n.getMyDefaultPort()==node.getMyDefaultPort())) {
                hasNode = true;
                break;
            }
        }

        if (!hasNode) {
            connectedNodeList.add(node);
            controller.addNeighbour(node);
        }

        return hasNode;

    }

    private String connectToBS() {

        Message registerMsg = new Register(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), node.getMyUsername());
        MessageClient messageClient = new MessageClient();
        String errorValue = "ERROR";

        try {
            String receivedMessage = messageClient.sendMessage(bootStrapIp, bootStrapPort, registerMsg);
            MessageDecoder messageDecoder = new MessageDecoder();
            Message message = messageDecoder.decodeMessage(receivedMessage);

            if (message instanceof AckRegister) {
                AckRegister ackRegister = (AckRegister) message;
                int ackValue = ackRegister.getNoNodes();


                switch (ackValue) {
                    case 9999:
                        errorValue = "There is some error in the command";
                        break;
                    case 9998:
                        errorValue = "Already registered to you, unregister first";
                        break;
                    case 9997:
                        errorValue = "Registered to another user, try a different IP and port";
                        break;
                    case 9996:
                        errorValue = "Can’t register. BS full";
                        break;
                    default:
                        if (ackRegister.getNoNodes() > 0) {
                            Node node1 = new Node(ackRegister.getIp1(), Integer.parseInt(ackRegister.getPort1()), ackRegister.getUserName1());
                            connectedNodeList.add(node1);
                            if (ackRegister.getNoNodes() == 2) {
                                Node node2 = new Node(ackRegister.getIp2(), Integer.parseInt(ackRegister.getPort2()), ackRegister.getUserName2());
                                connectedNodeList.add(node2);
                            }
                        }
                        errorValue = "Successful";
                        break;
                }


            }

            joinToNodes();
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return errorValue;
    }

    private void joinToNodes() {

        for (int i = 0; i < connectedNodeList.size(); i++) {

            String nodeIp = connectedNodeList.get(i).getMyIp();
            int nodePort = connectedNodeList.get(i).getMyDefaultPort();
            String nodeUserName = connectedNodeList.get(i).getMyUsername();

            Message joinMsg = new Join(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), node.getMyUsername());
            UDPClient messageClient = new UDPClient();

            messageClient.sendMessage(nodeIp, nodePort, joinMsg);
//                MessageDecoder messageDecoder = new MessageDecoder();
//                Message message = messageDecoder.decodeMessage(receivedMessage);
//
//                if (message instanceof AckJoin) {
//                    AckJoin ackJoin = (AckJoin) message;
//                    if (ackJoin.getValue() == 9999) {
//                        connectedNodeList.remove(i);
//                    }
//                }
            controller.addNeighbour(connectedNodeList.get(i));
        }

    }

    private void addFilesToNode() {
        node.addFileList(fileRepo.getFilesFromRepo());
    }

    private ArrayList<String> getNodeQueryResults(String query) {
        return node.isFileInRepo(query);
    }

    // calls from ui to check from node's file list, otherwise send to other nodes
    public void getQueryResults(String query) {

        boolean hasFile = false;
        ArrayList<String> results = node.isFileInRepo(query);
        queryResults = new HashMap<String, String[]>();

        if (!results.isEmpty()) {
            queryResults.put("ThisNode", results.toArray(new String[results.size()]));
        }

        Random random = new Random();
        int nodeSize = connectedNodeList.size();
        int nodeCount = 2;       // select two nodes to send

        if (nodeSize < 2) {
            nodeCount = nodeSize;
        }

        if (nodeSize > 0) {
            isTimerOn = true;
            startTimer();
        } else {
            controller.showSearchResults(queryResults);
        }

        ArrayList<Integer> sentNodes = new ArrayList<Integer>();

        while (nodeCount > 0) {
            int nodeId = random.nextInt(nodeSize);
            boolean hasId = false;
            for (int i = 0; i < sentNodes.size(); i++) {

                if (nodeId == sentNodes.get(i)) {
                    hasId = true;
                    break;
                }
            }

            if (!hasId) {
                String nodeIp = connectedNodeList.get(nodeId).getMyIp();
                int nodePort = connectedNodeList.get(nodeId).getMyDefaultPort();
                Message searchMsg = new Search(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), query, TOTAL_HOP_COUNT);
                UDPClient messageClient = new UDPClient();
                messageClient.sendMessage(nodeIp, nodePort, searchMsg);
                nodeCount--;
            }
        }



    }

    private void startTimer() {
        searchTimer = new Timer();

        searchTimer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        isTimerOn = false;
                        controller.showSearchResults(queryResults);
                    }
                });
            }
        }, TIMER_SECONDS * 1000);
    }

    private void stopTimer() {
        searchTimer.cancel();
    }
}
