package app;

import java.util.ArrayList;

/**
 * Created by lasitha on 3/11/15.
 */
public interface DSManagerInterface {

    public void sendLeaveMessages();
    public ArrayList<String> getNodeFileList();
    public String start();
    public void getQueryResults(String query);
}
