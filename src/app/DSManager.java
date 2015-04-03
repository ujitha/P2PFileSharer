package app;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by lasitha on 3/11/15.
 */
public abstract class DSManager {

    public abstract void sendLeaveMessages();
    public abstract ArrayList<String> getNodeFileList();
    public abstract String start();
    public abstract void getQueryResults(String query);
    public abstract void printQueryValues();
    public String generateUserName(String ip)
    {
        String[] ipValues = ip.split("\\.");
        String userName = Integer.parseInt(ipValues[ipValues.length-1])+100+"";
        Random random = new Random();
        userName+=String.format("%04d",random.nextInt(10000));

        return userName;
    }


}
