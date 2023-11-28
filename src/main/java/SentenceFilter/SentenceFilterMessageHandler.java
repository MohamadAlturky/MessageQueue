package SentenceFilter;

import MessageQueue.Utilities.Message;
import MessageQueue.Utilities.MessageHandler;

import java.util.HashMap;
import java.util.Objects;

public class SentenceFilterMessageHandler implements MessageHandler<String> {

    private static final String[] _questionWords = {"what", "which", "who", "where", "why", "when", "how", "whose"};

    private Message<String> _message;

    @Override
    public void setMessage(Message<String> message) {
        _message = message;
    }

    private String result = "";

    @Override
    public String getResult() {
        var answer = result;
        result = "";
        return answer;
    }

    @Override
    public void handle() {
        for (String word : _message.payload.split(" ")) {
            for(String questionWord : _questionWords){
                if (Objects.equals(questionWord, word)) {
                    result =  result.concat(word) + " ";
                }
            }
        }
    }
}
