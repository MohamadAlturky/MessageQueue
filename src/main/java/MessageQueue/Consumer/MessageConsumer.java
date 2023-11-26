package MessageQueue.Consumer;

import MessageQueue.Exceptions.NoChannelException;
import MessageQueue.Exceptions.NoNewMessageException;
import MessageQueue.Exceptions.SerializationException;
import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Serialization.PayloadSerializer;
import MessageQueue.Utilities.Message;
import MessageQueue.Utilities.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class MessageConsumer implements Consumer {
    private final MessageQueue _messageQueue;
    private final PayloadSerializer _payloadSerializer;
    private final List<Integer> _ports;
    private boolean _listening = true;
    private final MessageHandler _messageHandler;
    private List<Thread> _threads = new ArrayList<>();

    public MessageConsumer(MessageQueue messageQueue,
                           PayloadSerializer payloadSerializer,
                           List<Integer> ports,
                           MessageHandler messageHandler) {
        _messageQueue = messageQueue;
        _payloadSerializer = payloadSerializer;
        _ports = ports;
        _messageHandler = messageHandler;
    }


    public void start() {
        for (Integer port : _ports) {
            Thread thread = new Thread(() -> {
                try {
                    this.listen(port);
                } catch (NoChannelException | SerializationException | NoNewMessageException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            _threads.add(thread);
            thread.start();
        }
    }


    @Override
    public <E> E getResultAfterTermination() throws InterruptedException {
        for (Thread thread : _threads) {
            thread.join();
        }
        return (E) _messageHandler.getResult();
    }



    @Override
    public List<Integer> getPorts() {
        return _ports;
    }

    private void listen(Integer port) throws NoChannelException, SerializationException, NoNewMessageException, InterruptedException {
        while (_messageQueue.isRunning || _messageQueue.channelsHaveMessage(port)) {
            var message = _messageQueue.getMessage(port);

            if (message == null && !_messageQueue.isRunning) {
                System.out.println("Breaking");
                break;
            }
            Class<Message> type = Message.class;
            _messageHandler.setMessage(_payloadSerializer.deserialize(message, type));
            _messageHandler.handle();
        }
    }
}
