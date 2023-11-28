package MessageQueue.Queuing;

import MessageQueue.Exceptions.NoChannelException;

import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
    public static final String END_OF_STREAM = "END_OF_STREAM";
    private final Hashtable<Integer, ConcurrentLinkedQueue<String>> _channels = new Hashtable<>();
    public final Object _consumersLock = new Object();
    public final Object _terminationLock = new Object();
    public boolean isRunning = true;

    public void initializeChannel(Integer port) {
        if (_channels.get(port) == null) {
            _channels.put(port, new ConcurrentLinkedQueue<>());
        }
    }

    public void submitMessage(String json, Integer port) throws NoChannelException {
        if (_channels.get(port) == null) {
            throw new NoChannelException();
        }
        synchronized (_consumersLock) {
            _channels.get(port).add(json);
            _consumersLock.notifyAll();
        }
    }
    public boolean channelsHaveMessage(Integer port) {
        return !_channels.get(port).isEmpty();
    }

    MessageQueue() {

    }

    public String getMessage(Integer port) throws InterruptedException, NoChannelException {
        String msg = "";
        synchronized (_consumersLock) {
            if (_channels.get(port) == null) {
                throw new NoChannelException("No channel on port " + port.toString());
            }

            while (_channels.get(port).isEmpty() && isRunning) {
                _consumersLock.wait();
                System.out.println("Thread " + Thread.currentThread().getName() + " Waited On " + port);
            }
            if (_channels.get(port).isEmpty() && !isRunning) {
                System.out.println("Thread " + Thread.currentThread().getName() + " Terminated On " + port);
                return END_OF_STREAM;
            }
            msg = _channels.get(port).poll();
            _consumersLock.notifyAll();

        }
        synchronized (_terminationLock) {
            _terminationLock.notifyAll();
        }
        return msg;
    }


    public void waitForTermination() throws InterruptedException {
        isRunning = false;

        synchronized (_terminationLock) {
            for (ConcurrentLinkedQueue queue : _channels.values()) {
                while (!queue.isEmpty()) {
                    _terminationLock.wait();
                }
                queue.clear();
                System.out.println("the queue terminated successfully " + queue.getClass().getName());
            }
            synchronized (_consumersLock) {
                _consumersLock.notifyAll();
            }
        }

    }
}
