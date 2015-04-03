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
public class UDPDSManager extends DSManager {

    public String bootStrapIp;
    public int bootStrapPort;
    public Node node;
    public FileRepo fileRepo;
    private int MY_DEFAUILT_PORT = 6000;
    private int TIMER_SECONDS = 2;
    private List<Node> bsNodeList;
    private List<Node> connectedNodeList;
    private UDPserver server;
    private int TOTAL_HOP_COUNT = 5;
    private boolean isTimerOn = false;
    private HashMap<String, String[]> queryResults;
    private FSViewController controller;
    private Timer searchTimer;
    private int globalHopCount=0;
    private int receivedQs = 0;
    private int forwardedQs = 0;
    private int answeredQs = 0;
    private int routingMsgs = 0;


    //For Performance Evaluation
    Long startTime;
    Long endTime;

    // Constructor for having default port number
    public UDPDSManager(String bootStrapIp, int bootStrapPort, String myIp, FSViewController controller) {
        this.bootStrapIp = bootStrapIp;
        this.bootStrapPort = bootStrapPort;
        this.controller = controller;

        String username = "user:" + myIp;
        node = new Node(myIp, MY_DEFAUILT_PORT, username);
        fileRepo = new FileRepo();
        addFilesToNode();
        this.bsNodeList = new ArrayList<Node>();
        this.connectedNodeList = new ArrayList<Node>();
    }

    // Constructor for having an overriding port number
    public UDPDSManager(String bootStrapIp, int bootStrapPort, String myIp, int myPort, FSViewController controller) {
        this.bootStrapIp = bootStrapIp;
        this.bootStrapPort = bootStrapPort;
        this.controller = controller;


        node = new Node(myIp, myPort, generateUserName(myIp));
        fileRepo = new FileRepo();
        addFilesToNode();
        this.bsNodeList = new ArrayList<Node>();
        this.connectedNodeList = new ArrayList<Node>();
        this.bsNodeList = new ArrayList<Node>();
    }

    // Calls from UI when leaving the network to inform other nodes
    public void sendLeaveMessages() {

        Message unregMessage = new Unregister(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), node.getMyUsername());
        MessageClient messageClient = new MessageClient();
        routingMsgs++;

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
            routingMsgs++;

        }

        connectedNodeList.clear();
        bsNodeList.clear();
        server.closeSocket();
    }

    public void printQueryValues()
    {
        controller.writeToLog("Answered Querys - "+answeredQs);
        controller.writeToLog("Forwarded Querys - "+forwardedQs);
        controller.writeToLog("Received Querys - "+receivedQs);
        controller.writeToLog("Routing Querys - "+routingMsgs);
        controller.writeToLog("Routing Table Size - "+connectedNodeList.size());


    }

    // Returns node's file list
    public ArrayList<String> getNodeFileList() {
        return this.node.getFiles();
    }

    // Call from UI to connect
    public String start() {

        answeredQs = 0;
        receivedQs = 0;
        forwardedQs = 0;
        routingMsgs = 0;

        server = new UDPserver(node.getMyIp(), node.getMyDefaultPort(), new MessageCallback() {
            @Override
            public void receiveMessage(String message) {

                controller.writeToLog(message);
                MessageDecoder msDecoder = new MessageDecoder();
                Message incomingMsg = msDecoder.decodeMessage(message);

                if (incomingMsg instanceof Join) {
                    Join joinNode = (Join) incomingMsg;
                    routingMsgs++;
                    processJoinMsg(joinNode);

                } else if (incomingMsg instanceof AckLeave) {

                    AckLeave leaveMsg = (AckLeave) incomingMsg;
                    routingMsgs++;
                    int leaveValue = leaveMsg.getValue();
                    if (leaveValue == 0) {
                        //ok to leave
                    } else {
                        // not ok to leave
                    }

                } else if (incomingMsg instanceof Leave) {

                    Leave leaveMsg = (Leave) incomingMsg;
                    routingMsgs++;
                    processLeaveMsg(leaveMsg);

                } else if (incomingMsg instanceof Search) {
                    Search searchMsg = (Search) incomingMsg;
                    receivedQs++;
                    processSearchMsg(searchMsg);

                } else if (incomingMsg instanceof AckSearch) {

                    AckSearch searchAck = (AckSearch) incomingMsg;

                    if (isTimerOn) {
                        int ackValue = searchAck.getNoOfFiles();
                        globalHopCount += searchAck.getHops();
                        stopTimer();

                        if ((ackValue > 0)&&(ackValue < 9998)) {
                            endTime = System.currentTimeMillis();
                            controller.writeToLog("First Search time : "+(endTime-startTime)+"ms");
                            String ip = searchAck.getIp();
                            String[] results = searchAck.getFileNames();
                            queryResults.put(ip, results);
                            controller.showSearchResults(queryResults,2);
                            queryResults.clear();
                        }

                        startTimer();
                    }

                } else if (incomingMsg instanceof AckJoin) {

                    AckJoin ackJoin = (AckJoin) incomingMsg;
                    routingMsgs++;

                    int value = ackJoin.getValue();
                    String ip = ackJoin.getIp();
                    int port = Integer.parseInt(ackJoin.getPort());

                    for (int i = 0; i < bsNodeList.size(); i++) {

                        if (bsNodeList.get(i).getMyIp().equals(ip) && bsNodeList.get(i).getMyDefaultPort() == port) {
                            if (value == 0) {
                                connectedNodeList.add(bsNodeList.get(i));
                                controller.addNeighbour(bsNodeList.get(i));
                            }
                           
                        }
                    }

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
        Node newNode = new Node(ip, Integer.parseInt(port), userName);
        boolean insertInTable = addNodeToList(newNode);
        int resValue = 0;
        if (insertInTable) {
            resValue = 9999;
        }

        Message joinAck = new AckJoin(resValue, node.getMyIp(), Integer.toString(node.getMyDefaultPort()));
        UDPClient messageClient = new UDPClient();
        controller.writeToLog("Sent AckJoin to - "+ip+"-"+port);

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
        if (ip.equals(node.getMyIp()) && port.equals(Integer.toString(node.getMyDefaultPort()))) {
            globalHopCount += TOTAL_HOP_COUNT - hopSize;
            return;
        }

        if (!results.isEmpty()) {

            answeredQs++;
            Message searchAck = new AckSearch(results.size(), node.getMyIp(), Integer.toString(node.getMyDefaultPort()), TOTAL_HOP_COUNT - hopSize, results.toArray(new String[results.size()]));
            UDPClient messageClient = new UDPClient();
            messageClient.sendMessage(ip, Integer.parseInt(port), searchAck);

        } else {

            if (hopSize <= 0) {
                answeredQs++;
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
                searchMsg.reduceHopCount();
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
                        sentNodes.add(nodeId);
                        forwardedQs++;
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
            if ((n.getMyIp().equals(nodeIp)) && (n.getMyDefaultPort() == Integer.parseInt(nodePort))) {
                leaveValue = 0;
                controller.removeNeighbour(connectedNodeList.get(i));
                connectedNodeList.remove(i);
                break;
            }
        }

        Message leaveAck = new AckLeave(leaveValue, node.getMyIp(), Integer.toString(node.getMyDefaultPort()));
        UDPClient messageClient = new UDPClient();

        messageClient.sendMessage(nodeIp, Integer.parseInt(nodePort), leaveAck);
    }

    private boolean addNodeToList(Node node) {
        boolean hasNode = false;
        for (int i = 0; i < connectedNodeList.size(); i++) {
            Node n = connectedNodeList.get(i);
            if ((n.getMyIp().equals(node.getMyIp())) && (n.getMyDefaultPort() == node.getMyDefaultPort())) {
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
            routingMsgs++;
            MessageDecoder messageDecoder = new MessageDecoder();
            Message message = messageDecoder.decodeMessage(receivedMessage);

            if (message instanceof AckRegister) {
                AckRegister ackRegister = (AckRegister) message;
                int ackValue = ackRegister.getNoNodes();
                routingMsgs++;

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
                            bsNodeList.add(node1);
                            if (ackRegister.getNoNodes() == 2) {
                                Node node2 = new Node(ackRegister.getIp2(), Integer.parseInt(ackRegister.getPort2()), ackRegister.getUserName2());
                                bsNodeList.add(node2);
                            }
                        }
                        errorValue = "Successful";
                        break;
                }


            }

            if(errorValue.equals("Successful"))
            {
                joinToNodes();
                server.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return errorValue;
    }

    private void joinToNodes() {

        for (int i = 0; i < bsNodeList.size(); i++) {

            String nodeIp = bsNodeList.get(i).getMyIp();
            int nodePort = bsNodeList.get(i).getMyDefaultPort();
            String nodeUserName = bsNodeList.get(i).getMyUsername();

            Message joinMsg = new Join(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), node.getMyUsername());
            UDPClient messageClient = new UDPClient();
            routingMsgs++;

            messageClient.sendMessage(nodeIp, nodePort, joinMsg);

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

        globalHopCount = 0;
        controller.writeToLog("Searched for : "+query);
        startTime = System.currentTimeMillis();
        endTime = 0L;

        boolean hasFile = false;
        ArrayList<String> results = node.isFileInRepo(query);
        queryResults = new HashMap<String, String[]>();

        if (!results.isEmpty()) {
            queryResults.put(node.getMyIp()+" - This Node", results.toArray(new String[results.size()]));
            endTime = System.currentTimeMillis();
        }
        controller.showSearchResults(queryResults,1);
        queryResults.clear();

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
            if(endTime<startTime){
                endTime = System.currentTimeMillis();
            }
            controller.writeToLog("Total Search time : "+(endTime-startTime)+"ms");
            controller.writeToLog("Total Hop count : "+(globalHopCount));
            controller.showSearchResults(queryResults, 3);
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
                sentNodes.add(nodeId);
                String nodeIp = connectedNodeList.get(nodeId).getMyIp();
                int nodePort = connectedNodeList.get(nodeId).getMyDefaultPort();
                Message searchMsg = new Search(node.getMyIp(), Integer.toString(node.getMyDefaultPort()), query, TOTAL_HOP_COUNT);
                controller.writeToLog("Sent Search Msg to - "+nodeIp+"-"+nodePort+" - "+searchMsg.toString());
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
                        if(endTime<startTime){
                            endTime = System.currentTimeMillis();
                        }
                        controller.writeToLog("Total Search time : "+(endTime-startTime)+"ms");
                        controller.writeToLog("Total Hop count : "+(globalHopCount));
                        controller.showSearchResults(queryResults, 3);
                    }
                });
            }
        }, TIMER_SECONDS * 1000);
    }

    private void stopTimer() {
        searchTimer.cancel();
    }
}
