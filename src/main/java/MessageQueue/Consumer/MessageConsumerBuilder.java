package MessageQueue.Consumer;

import MessageQueue.Exceptions.MissingBuildAttributeException;
import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Serialization.JacksonPayloadSerializer;
import MessageQueue.Serialization.PayloadSerializer;
import MessageQueue.Utilities.Builder;
import MessageQueue.Utilities.MessageHandler;

import java.util.List;

public class MessageConsumerBuilder implements Builder<MessageConsumer> {
    private MessageQueue _messageQueue;
    private List<Integer> _ports;
    private MessageHandler _messageHandler;
    private PayloadSerializer _payloadSerializer = new JacksonPayloadSerializer();


    public void setPayloadSerializer(PayloadSerializer payloadSerializer) {
        this._payloadSerializer = payloadSerializer;
    }



    public void setMessageQueue(MessageQueue messageQueue) {
        this._messageQueue = messageQueue;
    }

    public void setPorts(List<Integer> ports) {
        this._ports = ports;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        _messageHandler = messageHandler;
    }

    @Override
    public MessageConsumer build() throws MissingBuildAttributeException {
        if (_messageHandler == null) {
            throw new MissingBuildAttributeException("Please specify a MessageHandler attribute");
        }
        if (_ports == null) {
            throw new MissingBuildAttributeException("Please specify a ports attribute");
        }
        if (_messageQueue == null) {
            throw new MissingBuildAttributeException("Please specify a messageBroker attribute");
        }
        return new MessageConsumer(_messageQueue, _payloadSerializer, _ports, _messageHandler);
    }

}
