package MessageQueue.MessageInterception;

import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Serialization.PayloadSerializer;
import MessageQueue.Utilities.Message;
import MessageQueue.Utilities.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InterceptorMessageConsumer implements InterceptorConsumer {
    private final MessageQueue _messageQueue;
    private final PayloadSerializer _payloadSerializer;
    private final Interceptor _parent;
    private final List<Integer> _ports;
    private final MessageHandler _messageHandler;
    private List<Thread> _threads = new ArrayList<>();
    private boolean _finished = false;

    public InterceptorMessageConsumer(MessageQueue messageQueue,
                                      PayloadSerializer payloadSerializer,
                                      Interceptor parent, List<Integer> ports,
                                      MessageHandler messageHandler) {
        _messageQueue = messageQueue;
        _payloadSerializer = payloadSerializer;
        _parent = parent;
        _ports = ports;
        _messageHandler = messageHandler;
    }

    @Override
    public void start() {
        for (Integer port : _ports) {
            Thread thread = new Thread(() -> {
                try {
                    this.listen(port);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            _threads.add(thread);
            thread.start();
        }
    }

    @Override
    public boolean finished() {
        return _finished;
    }

    private PipeLine _pipeLine;

    @Override
    public void setPipeLine(PipeLine pipeLine) {
        _pipeLine = pipeLine;
    }


    @Override
    public List<Integer> getPorts() {
        return _ports;
    }

    private void listen(Integer port) throws Exception {
//        while (_messageQueue.isRunning || _messageQueue.channelsHaveMessage(port)) {
        while (true) {
            var message = _messageQueue.getMessage(port);
            if (Objects.equals(message, MessageQueue.END_OF_STREAM) && !_messageQueue.isRunning) {
                _finished = true;
                _pipeLine.notifyFinishing();
                break;
            }
            Class<Message> type = Message.class;
            _messageHandler.setMessage(_payloadSerializer.deserialize(message, type));
            _messageHandler.handle();
            var result = _messageHandler.getResult();
            _parent.send(result, port);
        }
    }
}
