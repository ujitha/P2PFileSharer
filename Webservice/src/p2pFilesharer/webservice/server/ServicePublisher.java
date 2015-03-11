package p2pFilesharer.webservice.server;

import app.DSManagerInterface;


import javax.xml.ws.Endpoint;
import java.util.ArrayList;

/**
 * Created by ujitha on 3/11/15.
 */
public class ServicePublisher {
    Endpoint e;
    public ServicePublisher(String ip,String port, DSManagerInterface dsManager){
        String address = "http://"+ip+":"+port+"/p2pFilesharer.webservice.server.ServiceReceiver";
        e= Endpoint.publish(address, new ServiceReceiverImpl(dsManager));
        System.out.println("Service is published");
    }

    public void stopService(){
        e.stop();
    }

//    public static void main(String[] args){
//        String address = "http://10.8.108.135:5100/p2pFilesharer.webservice.server.ServiceReceiver";
//      Endpoint  e= Endpoint.publish(address, new ServiceReceiverImpl(new DSManagerInterface() {
//            @Override
//            public void sendLeaveMessages() {
//
//            }
//
//            @Override
//            public ArrayList<String> getNodeFileList() {
//                return null;
//            }
//
//            @Override
//            public String start() {
//                return null;
//            }
//
//            @Override
//            public void getQueryResults(String query) {
//
//            }
//        }));
//    }
}
