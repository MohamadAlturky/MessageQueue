package MessageQueue.Exceptions;

public class NoSuchChannelException extends Exception {
    public NoSuchChannelException(Integer port) {
        super("No such channel on port " + port);
    }
}
