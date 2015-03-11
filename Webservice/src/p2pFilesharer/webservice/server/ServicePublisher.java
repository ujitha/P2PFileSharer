package p2pFilesharer.webservice.server;

import app.DSManagerInterface;


import javax.xml.ws.Endpoint;

/**
 * Created by ujitha on 3/11/15.
 */
public class ServicePublisher {

    public ServicePublisher(String ip,String port, DSManagerInterface dsManager){
        String address = "http://"+ip+":"+port+"/p2pFilesharer.webservice.server.ServiceReceiver";
        Endpoint.publish(address, new ServiceReceiverImpl(dsManager));
    }


}
