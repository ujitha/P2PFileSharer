package p2pFilesharer.webservice.server;

import app.DSManagerInterface;

import javax.jws.WebService;

/**
 * Created by ujitha on 3/11/15.
 */

@WebService(endpointInterface = "p2pFilesharer.webservice.server.ServiceReceiver")
public class ServiceReceiverImpl implements ServiceReceiver {
    DSManagerInterface dsManager;

    public ServiceReceiverImpl(){};

    public ServiceReceiverImpl(DSManagerInterface dsManager){
        this.dsManager=dsManager;
    }

    @Override
    public void sendMessage(String message) {
        //TODO pass the message to DSmanager
    }
}