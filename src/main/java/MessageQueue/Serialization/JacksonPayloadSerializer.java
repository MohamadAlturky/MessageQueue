package MessageQueue.Serialization;

import MessageQueue.Exceptions.SerializationException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonPayloadSerializer implements PayloadSerializer {
    private ObjectMapper mapper = new ObjectMapper();

    public Object serialize(Object payload) throws SerializationException {
        try {
            return mapper.writeValueAsString(payload);
        } catch (IOException e) {
            throw new SerializationException("Could not serialize object using Jackson.");
        }
    }

    public <T> T deserialize(String payload, Class<T> type) throws SerializationException {
        if (payload == null) {
            return null;
        }

        try {
            return mapper.readValue(payload, type);
        } catch (IOException e) {
            throw new SerializationException("Could not serialize object using Jackson."+e.getMessage());
        }
    }
}
