package SentenceFilter;

import MessageQueue.Utilities.Message;
import MessageQueue.Utilities.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class StringCollectorMessageHandler implements MessageHandler<String> {

    public StringCollectorMessageHandler() {
    }

    private Message<String> _message;
    private List<Message<String>> _results = new ArrayList<>();

    @Override
    public void setMessage(Message<String> message) {
        _message = message;
    }

    @Override
    public List<Message<String>> getResult() {
        return _results;
    }

    @Override
    public void handle() {
        _results.add(_message);
    }
}
