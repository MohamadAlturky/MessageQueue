package MessageQueue.MessageInterception;

import java.util.List;

public interface InterceptorConsumer {
    List<Integer> getPorts();
    void start();
    boolean finished();
    void setPipeLine(PipeLine pipeLine);
}
