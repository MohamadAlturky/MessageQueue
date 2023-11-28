package MessageQueue.MessageInterception;

import MessageQueue.Consumer.Consumer;
import MessageQueue.Producer.Producer;

public interface Interceptor extends Producer, InterceptorConsumer {
    boolean finished();
}
