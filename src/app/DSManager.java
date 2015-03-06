/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2pfilesharer;

import java.util.ArrayList;

/**
 *
 * @author Pubudu
 */
public class DSManager {

    public String bootStrapIp;
    public String bootStrapPort;
    public Node node;
    public FileRepo fileRepo;

    public DSManager(String ip, String port, Node node) {
        bootStrapIp = ip;
        bootStrapPort = port;
        fileRepo = new FileRepo();
        addFilesToNode();
        //  fileRepo = new ArrayList<String>();
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
