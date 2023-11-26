package MessageQueue.Utilities;

import java.io.Serializable;

public class Message<T> implements Serializable {
   public Message(int id, int port,T payload) {
        this.id = id;
        this.payload = payload;
        this.port = port;
    }

    public Message() {
    }

    public int id;
    public T payload;
    public int port;
}
