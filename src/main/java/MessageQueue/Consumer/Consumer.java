package MessageQueue.Consumer;

import java.util.List;

public interface Consumer {
    List<Integer> getPorts();

    void start();
    <E>E getResultAfterTermination() throws InterruptedException;
}
