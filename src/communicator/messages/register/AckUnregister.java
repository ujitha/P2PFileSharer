package communicator.messages.register;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckUnregister extends Message {
    /*
    length UNROK value

    e.g., 0012 UNROK 0
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    UNROK – Unregister response.
    value – Indicate success or failure.
        0 – successful
        9999 – error while unregistering. IP and port may not be in the registry or command is incorrect.
    */
    private int value;

    public AckUnregister(int value){
        this.value=value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        /*0012 UNROK 0*/
        String msg=" UNROK "+value;
        msg=String.format("%04d",msg.length())+msg;
        return msg;
    }
}
