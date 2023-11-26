package MessageQueue.Producer;

import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Serialization.PayloadSerializer;
import MessageQueue.Utilities.MessageFactory;

public class MessageProducer implements Producer{
    private final MessageQueue _messageQueue;
    private final PayloadSerializer _payloadSerializer;

    public MessageProducer(MessageQueue messageQueue, PayloadSerializer payloadSerializer) {
        _messageQueue = messageQueue;
        _payloadSerializer = payloadSerializer;
    }

    @Override
    public <T>void send(T payload,int port) throws Exception {
        var message = MessageFactory.createMessage(payload,port);
        _messageQueue.submitMessage(_payloadSerializer.serialize(message),port);
    }
}
