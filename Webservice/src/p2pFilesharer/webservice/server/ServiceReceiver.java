package p2pFilesharer.webservice.server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by ujitha on 3/11/15.
 */

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface ServiceReceiver {

    @WebMethod void sendMessage(String message);
}
