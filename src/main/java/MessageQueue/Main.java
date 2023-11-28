package MessageQueue;

import MessageQueue.Consumer.MessageConsumer;
import MessageQueue.Consumer.MessageConsumerBuilder;
import MessageQueue.Producer.MessageProducer;
import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Queuing.MessageQueueBuilder;
import MessageQueue.Serialization.JacksonPayloadSerializer;
import MessageQueue.Utilities.Message;
import SentenceFilter.StringCollectorMessageHandler;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        MessageQueueBuilder builder = new MessageQueueBuilder();
        MessageQueue queue = builder.build();

        queue.initializeChannel(22);
        MessageConsumerBuilder consumerBuilder = new MessageConsumerBuilder();

        consumerBuilder.setMessageQueue(queue);
        ArrayList<Integer> ports = new ArrayList<>();
        ports.add(22);
        consumerBuilder.setPorts(ports);
        consumerBuilder.setMessageHandler(new StringCollectorMessageHandler());
        MessageConsumer consumer= consumerBuilder.build();
        consumer.start();
        MessageProducer producer = new MessageProducer(queue,new JacksonPayloadSerializer());

        producer.send("hi",22);
        producer.send("we",22);
        producer.send("are very",22);
        producer.send("impessive",22);
        producer.send("hi",22);
        producer.send("hi",22);
        producer.send("hi",22);
        queue.waitForTermination();
        List<Message<String>>  x = consumer.getResultAfterTermination();

        for (Message xx : x){
            System.out.println(xx.payload);
        }
    }
}
