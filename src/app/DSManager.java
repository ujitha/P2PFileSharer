package app;

import java.util.ArrayList;

/**
 * Created by lasitha on 3/11/15.
 */
public abstract class DSManager {

    public abstract void sendLeaveMessages();
    public abstract ArrayList<String> getNodeFileList();
    public abstract String start();
    public abstract void getQueryResults(String query);
}
