package communicator.messages.search;

import communicator.messages.Message;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckSearch extends Message{

      /*
        length SEROK no_files IP port hops filename1 filename2 ... ...

        e.g., Suppose we are searching for string baby. So it will return, 0114 SEROK 3 129.82.128.1 2301 baby_go_home.mp3 baby_come_back.mp3 baby.mpeg
        length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
        SEROK – Sends the result for search. The node that sends this message is the one that actually stored the (key, value) pair, i.e., node that index the file information.
        no_files – Number of results returned
            ≥ 1 – Successful
            0 – no matching results. Searched key is not in key table
            9999 – failure due to node unreachable
            9998 – some other error.
        IP – IP address of the node having (stored) the file.
        port – Port number of the node having (stored) the file.
        hops – Hops required to find the file(s).
        filename – Actual name of the file.

         */
    private int noOfFiles;
    private String ip;
    private String port;
    private int hops;
    private String[] fileNames;

    public AckSearch(int noOfFiles, String ip, String port, int hops, String[] fileNames) {
        this.noOfFiles = noOfFiles;
        this.ip = ip;
        this.port = port;
        this.hops = hops;
        this.fileNames = fileNames;
    }

    public void setNoOfFiles(int noOfFiles) {
        this.noOfFiles = noOfFiles;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHops(int hops) {
        this.hops = hops;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    @Override
    public String toString() {
        throw new NotImplementedException();
    }
}
