package MessageQueue.MessageInterception;

import MessageQueue.Producer.MessageProducer;
import MessageQueue.Producer.Producer;
import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Serialization.JacksonPayloadSerializer;
import MessageQueue.Utilities.MessageHandler;

import java.util.List;

public class MessageInterceptor implements Interceptor {
    private MessageQueue _queueForConsuming;
    private MessageQueue _queueForProducing;
    private final InterceptorConsumer _consumer;
    private final Producer _producer;

    public MessageInterceptor(MessageQueue _queueForConsuming, MessageQueue _queueForProducing, List<Integer> ports, MessageHandler handler) {
        this._queueForConsuming = _queueForConsuming;
        this._queueForProducing = _queueForProducing;
        _producer = new MessageProducer(_queueForProducing,new JacksonPayloadSerializer());
        _consumer = new InterceptorMessageConsumer(_queueForConsuming,new JacksonPayloadSerializer(), this, ports,handler);
    }


    @Override
    public List<Integer> getPorts() {
        return _consumer.getPorts();
    }

    @Override
    public void start() {
        _consumer.start();
    }

    @Override
    public <T> void send(T payload, int port) throws Exception {
        _producer.send(payload, port);
    }
}