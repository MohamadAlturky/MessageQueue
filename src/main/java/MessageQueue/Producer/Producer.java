package MessageQueue.Producer;

public interface Producer {
   <T> void send(T payload,int port) throws Exception;
}
