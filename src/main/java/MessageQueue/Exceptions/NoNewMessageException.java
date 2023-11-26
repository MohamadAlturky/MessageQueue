package MessageQueue.Exceptions;

public class NoNewMessageException extends Exception {
    public NoNewMessageException() {
    }

    public NoNewMessageException(String s) {
        super(s);
    }
}
