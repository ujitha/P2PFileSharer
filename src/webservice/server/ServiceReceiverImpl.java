package webservice.server;

import app.DSManager;
import app.WebServiceDSManager;

import javax.jws.WebService;

/**
 * Created by ujitha on 3/11/15.
 */

@WebService(endpointInterface = "webservice.server.ServiceReceiver")
public class ServiceReceiverImpl implements ServiceReceiver {
    DSManager dsManager;

    public ServiceReceiverImpl(){};

    public ServiceReceiverImpl(DSManager dsManager){
        this.dsManager=dsManager;
    }

    @Override
    public void sendMessage(String message) {

        ((WebServiceDSManager)dsManager).receiveMessage(message);
    }
}
