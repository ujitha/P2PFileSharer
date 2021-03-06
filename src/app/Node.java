package app;

import communicator.MessageCallback;
import communicator.Server;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by ujitha on 3/5/15.
 */
public class Node {

    private String myIp;
    private int myDefaultPort;
    private String myUsername;
    private ArrayList<String> files;

    public Node(String myIp,int myDefaultPort,String myUsername){

        this.myIp=myIp;
        this.myDefaultPort=myDefaultPort;
        this.myUsername=myUsername;
        this.files = new ArrayList<String>();

    }

    public static void main(String[] args) {

        String file;
        file = "queries.txt";

        try{
            InputStream ips=new FileInputStream(file);
            InputStreamReader ipsr=new InputStreamReader(ips);
            BufferedReader br=new BufferedReader(ipsr);
            String line;

            while ((line=br.readLine())!=null){
                System.out.print("\"" + line + "\",");
                          }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public int getMyDefaultPort() {
        return myDefaultPort;
    }

    public String getMyUsername() {
        return myUsername;
    }

    public String getMyIp() {
        return myIp;
    }

    public StringProperty userNameProperty(){
        return new SimpleStringProperty(myUsername);
    }

    public StringProperty ipProperty(){
        return new SimpleStringProperty(myIp);
    }

    public StringProperty portProperty(){
        return new SimpleStringProperty(Integer.toString(myDefaultPort));
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    // Add node's file list
    public void addFileList(ArrayList<String> fileList) {
        files = fileList;
    }

    // check the file from node's file List
    public ArrayList<String> isFileInRepo(String fileName) {

        ArrayList<String> fileList = new ArrayList<String>();

        for (int i = 0; i < files.size(); i++) {

            fileName = fileName.toLowerCase();
            String fileQ = files.get(i).toLowerCase();

            if (fileQ.contains(fileName)) {
                fileList.add(files.get(i));
            }
        }

        for (int i = 0; i < fileList.size(); i++) {

            String[] tokens = fileName.split(" ");  //m of
            String[] results = fileList.get(i).split(" ");  //mic office
            boolean valid = false;

            for (int j = 0; j < tokens.length; j++) {
                for (int k = 0; k < results.length; k++) {

                    if (results[k].toLowerCase().equals(tokens[j].toLowerCase())) {
                        valid = true;
                        break;
                    } else {
                        valid = false;
                    }
                }
            }

            if (!valid) {
                fileList.remove(i);
            }
        }
        return fileList;
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
//       
}

