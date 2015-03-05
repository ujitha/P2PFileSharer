package communicator.messages.join;

import communicator.messages.Message;

/**
 * Created by lasitha on 3/5/15.
 */
public class AckJoin extends Message{
    /*
    length JOINOK value

    e.g., 0014 JOINOK 0
    length – Length of the entire message including 4 characters used to indicate the length. In xxxx format.
    JOINOK – Join response.
    value – Indicate success or failure
        0 – successful
        9999 – error while adding new node to routing table
     */
    private int value;

    public AckJoin(int value){
        this.value=value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
