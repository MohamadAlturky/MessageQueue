package MessageQueue.Queuing;

import MessageQueue.Utilities.Builder;

public class MessageQueueBuilder implements Builder<MessageQueue> {
    @Override
    public MessageQueue build() {
        return new MessageQueue();
    }
}
