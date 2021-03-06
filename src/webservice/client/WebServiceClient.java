package webservice.client;

import communicator.messages.Message;
//import webservice.stub.ServiceReceiver;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import webservice.server.ServiceReceiver;

/**
 * Created by ujitha on 3/11/15.
 */
public class WebServiceClient {

    public void sendMessage(String destinationIp, int port, Message message){

        String address="http://"+destinationIp+":"+port+"/webservice.server.ServiceReceiver?wsdl";

        URL url = null;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        QName qname = new QName("http://server.webservice/", "ServiceReceiverImplService");
        Service service = Service.create(url, qname);
        ServiceReceiver serviceReceiver=service.getPort(ServiceReceiver.class);

        serviceReceiver.sendMessage(message.toString());
    }
}
