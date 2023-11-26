package MessageQueue.Utilities;

public interface MessageHandler<T> {
    void setMessage(Message<T> message);
    <E> E getResult();
    void handle();
}
