package MessageQueue.Queuing;

import MessageQueue.Exceptions.NoChannelException;
import MessageQueue.Exceptions.QueueAttemptToStopException;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
    private final Hashtable<Integer, ConcurrentLinkedQueue<String>> _channels = new Hashtable<>();
    public final Object _consumersLock = new Object();
    public final Object _terminationLock = new Object();

    public boolean isRunning = true;
    private boolean _stopAcceptMessages = false;

    public void initializeChannel(Integer port){
        if (_channels.get(port) == null) {
            _channels.put(port, new ConcurrentLinkedQueue<>());
        }
    }

    public void submitMessage(String json, Integer port) throws NoChannelException, QueueAttemptToStopException {
        System.out.println(json + " submitted");
        if(_stopAcceptMessages){
            throw new QueueAttemptToStopException();
        }
        if (_channels.get(port) == null) {
            throw new NoChannelException();
        }
        synchronized (_consumersLock) {
            _channels.get(port).add(json);
            System.out.println("Added Json " + json);
            _consumersLock.notifyAll();
        }
    }

    public boolean channelsHaveMessage(List<Integer> ports) {
        for (Integer port : ports) {
            if (!_channels.get(port).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean channelsHaveMessage(Integer port) {
        return !_channels.get(port).isEmpty();
    }

    MessageQueue() {

    }

    public String getMessage(Integer port) throws InterruptedException, NoChannelException {

        synchronized (_consumersLock) {
            System.out.println("Thread + " + Thread.currentThread().getName());
            if (_channels.get(port) == null) {
                throw new NoChannelException("No channel on port " + port.toString());
            }

            while (_channels.get(port).isEmpty() && isRunning) {
                _consumersLock.wait();
                System.out.println("Waited");
            }

            if(_channels.get(port).isEmpty()) {
                System.out.println("nullnullnullnullnullnullnullnullnullnullnullnullnullnull");
                return null;
            }
            String msg = _channels.get(port).poll();
            System.out.println(msg +" taken");
            synchronized (_terminationLock) {
                _terminationLock.notifyAll();
            }
            _consumersLock.notifyAll();
            return msg;
        }
    }


    public void waitForTermination() throws InterruptedException {
        synchronized (_terminationLock) {
            _stopAcceptMessages = true;
            for (ConcurrentLinkedQueue queue : _channels.values()) {
                while (!queue.isEmpty()) {
                    _terminationLock.wait();
                }
                isRunning = false;
                System.out.println("the queue terminated successfully " + queue.getClass().getName());
            }
        }
        synchronized (_consumersLock){
            _consumersLock.notifyAll();
        }
    }
}
