package MessageQueue.Consumer;

import java.util.List;

public interface Consumer {
    List<Integer> getPorts();

    <E>E getResultAfterTermination() throws InterruptedException;
}
