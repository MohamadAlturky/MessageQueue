package SentenceFilter;

import FileUtilities.TextFileReader;
import FileUtilities.TextFileWriter;
import MessageQueue.Consumer.Consumer;
import MessageQueue.Consumer.MessageConsumer;
import MessageQueue.Producer.MessageProducer;
import MessageQueue.Producer.Producer;
import MessageQueue.Serialization.JacksonPayloadSerializer;
import MessageQueue.Utilities.Message;

import java.util.ArrayList;
import java.util.List;

public class SentenceFilter {
    private static final List<Integer> ports = new ArrayList<>();
    private static final String sourceFilePath = "file.txt";
    private static final String destinationFilePath = "new.txt";
    private static final Integer numberOfInterceptors = 10;
    private static final TextFileReader fileReader = new TextFileReader();
    private static final TextFileWriter fileWriter = new TextFileWriter();


    public static void main(String[] args) throws Exception {
        System.out.println("Main thread is listening on  " + Thread.currentThread().getName());
        // Arrange
        ports.add(22);

        SentenceFilteringPipeLine pipeLine =
                new SentenceFilteringPipeLine(numberOfInterceptors,
                        ports.get(0));

        List<String> lines = fileReader.readLines(sourceFilePath);

        // Pushing the lines into the pipeline.
        Producer messageProducer = new MessageProducer(pipeLine.getProducingQueue(), new JacksonPayloadSerializer());
        Consumer messageConsumer = new MessageConsumer(pipeLine.getConsumingQueue(), new JacksonPayloadSerializer(),
                ports, new StringCollectorMessageHandler());

        messageConsumer.start();
        pipeLine.startPipeline();

        for (String line : lines) {
            messageProducer.send(line, ports.get(0));
        }


        // wait for the consumer to collect all messages.

        pipeLine.waitForTermination();

        List<Message<String>> messages = messageConsumer.getResultAfterTermination();

        List<String> answer = new ArrayList<>();
        for (Message line : messages) {
            answer.add((String) line.payload);
        }
        fileWriter.writeLines(destinationFilePath, answer);
        System.out.println("Finished");
    }
}
