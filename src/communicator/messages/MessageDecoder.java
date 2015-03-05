package communicator.messages;

import communicator.messages.join.AckJoin;
import communicator.messages.join.Join;
import communicator.messages.leave.AckLeave;
import communicator.messages.leave.Leave;
import communicator.messages.register.AckRegister;
import communicator.messages.register.AckUnregister;
import communicator.messages.register.Register;
import communicator.messages.register.Unregister;
import communicator.messages.search.AckSearch;
import communicator.messages.search.Search;

/**
 * Created by lasitha on 3/5/15.
 */
public class MessageDecoder {

    public Message decodeMessage(String message){
        String type=message.split(" ")[1];
        Message m=null;
        if("JOINOK".equals(type)){
            m=new AckJoin(message);
        }
        else if("JOIN".equals(type)){
            m=new Join(message);
        }else if("LEAVEOK".equals(type)){
            m=new AckLeave(message);
        }else if("LEAVE".equals(type)){
            m=new Leave(message);
        }else if("REGOK".equals(type)){
            m=new AckRegister(message);
        }else if("UNROK".equals(type)){
            m=new AckUnregister(message);
        }else if("REG".equals(type)){
            m=new Register(message);
        }else if("UNREG".equals(type)){
            m=new Unregister(message);
        }else if("SEROK".equals(type)){
            m=new AckSearch(message);
        }else if("SER".equals(type)){
            m=new Search(message);
        }else if("ERROR".equals(type)){
            m=new Error();
            m.decodeMessage(message);
        }else{
            m=new Error("Unknown message");
        }
        return m;
    }
}
