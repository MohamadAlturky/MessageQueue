package MessageQueue.Exceptions;


public class ChannelAlreadyExistsException extends Exception {
    public ChannelAlreadyExistsException(Integer port) {
        super("channel already exists on port " + port);
    }
}
