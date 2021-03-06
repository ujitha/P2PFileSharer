package webservice.server;

import app.DSManager;

import javax.xml.ws.Endpoint;

/**
 * Created by ujitha on 3/11/15.
 */
public class ServicePublisher {
    Endpoint e;
    public ServicePublisher(String ip,String port, DSManager dsManager){
        String address = "http://"+ip+":"+port+"/webservice.server.ServiceReceiver";
        e= Endpoint.publish(address, new ServiceReceiverImpl(dsManager));
        System.out.println("Service is published : "+address);
    }

    public void stopService(){
        e.stop();
        System.out.println("Service stopped");
    }

//    public static void main(String[] args){
//        String address = "http://10.8.108.13:5100/p2pFilesharer.webservice.server.ServiceReceiver";
//      Endpoint  e= Endpoint.publish(address, new ServiceReceiverImpl(new DSManager() {
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
