package MessageQueue.Utilities;

public class MessageFactory<T> {
    private static int _counter = 0;
    private static final Object _counterLock = new Object();
    public static<T>  Message<T> createMessage(T payload,int port) {
        Message<T> message;
        synchronized (_counterLock) {
            message= new Message<T>(_counter++,port,payload);
        }
        return message;
    }
}