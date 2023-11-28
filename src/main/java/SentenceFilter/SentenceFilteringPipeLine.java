package SentenceFilter;

import MessageQueue.MessageInterception.Interceptor;
import MessageQueue.MessageInterception.MessageInterceptor;
import MessageQueue.MessageInterception.PipeLine;
import MessageQueue.Queuing.MessageQueue;
import MessageQueue.Queuing.MessageQueueBuilder;
import MessageQueue.Utilities.MessageHandler;

import java.util.ArrayList;
import java.util.List;

public class SentenceFilteringPipeLine implements PipeLine {
    private final MessageQueue _firstQueue;
    private final MessageQueue _secondQueue;
    private final List<Interceptor> _interceptors;
    private final Object _interceptorsWorkingLock = new Object();

    public SentenceFilteringPipeLine(Integer numberOfInterceptors, Integer port) {
        _interceptors = new ArrayList<>();
        MessageQueueBuilder builder = new MessageQueueBuilder();
        this._firstQueue = builder.build();
        this._secondQueue = builder.build();
        _secondQueue.initializeChannel(port);
        _firstQueue.initializeChannel(port);
        for(int i = 0; i < numberOfInterceptors; i++){
            List<Integer> ports = new ArrayList<>();
            ports.add(port);
            MessageHandler interceptorsMessageHandler = new SentenceFilterMessageHandler();
            Interceptor interceptor = new MessageInterceptor(_firstQueue, _secondQueue,ports,interceptorsMessageHandler,this);
            _interceptors.add(interceptor);
        }
    }

    public MessageQueue getConsumingQueue() {
        return _secondQueue;
    }
    public void startPipeline() {
        for(Interceptor interceptor : _interceptors){
            interceptor.start();
        }
    }
    public MessageQueue getProducingQueue() {
        return _firstQueue;
    }

    public void waitForTermination() throws InterruptedException {
        _firstQueue.waitForTermination();
        synchronized (_interceptorsWorkingLock) {
            for(Interceptor interceptor : _interceptors) {
                while (!interceptor.finished()){
                    _interceptorsWorkingLock.wait();
                }
            }
        }

        _secondQueue.waitForTermination();
        System.out.println("the pipeline terminated successfully");
    }

    @Override
    public void notifyFinishing() {
        synchronized (_interceptorsWorkingLock){
            _interceptorsWorkingLock.notifyAll();
        }
    }
}
